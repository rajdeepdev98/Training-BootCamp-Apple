
//import org.apache.spark.streaming.kafka010._
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.protobuf.functions.{from_protobuf, to_protobuf}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructType}

import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

object SensorDataStreamingJob {
  val GCP_CREDENTIALS=sys.env.getOrElse("GCP_CREDENTIALS", "")
  val bucketName = "de_case_study_bucket"
  val rawDataDescriptorFile = "src/main/resources/descriptors/SensorReading.desc"
  val aggrDataDescriptorFile = "src/main/resources/descriptors/AggrDaata.desc"

  //protobuf message type -> fully qualified
  val rawDataMessageType = "protos.SensorReading"
  val aggrDataMessageType= "protos.AggrData"
  val aggrSchema = new StructType()
    .add("sensorId", StringType)
    .add("avgTemperature", FloatType)
    .add("avgHumidity", FloatType)
    .add("minTemperature", FloatType)
    .add("maxTemperature", FloatType)
    .add("minHumidity", FloatType)
    .add("maxHumidity", FloatType)
    .add("dataCount", IntegerType)


  val spark: SparkSession = SparkSession.builder()
    .appName("SensorDataAggregation")
    .config("spark.hadoop.fs.defaultFS", "gs://de_case_study_bucket/")
    .config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
    .config("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
    .config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
    .config("spark.hadoop.google.cloud.auth.service.account.json.keyfile", GCP_CREDENTIALS)
    .master("local[*]")
    .getOrCreate()
  import spark.implicits._

  def filterData(df: DataFrame): DataFrame = {

    df.filter($"temperature" > -10 && $"temperature" < 50 && $"humidity" > 0 && $"humidity" < 100)
  }
  def main(args: Array[String]): Unit = {



    val kafkaBootstrapServers = "localhost:9092"
    val topic = "sensor-readings"



    val kafkaDF = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaBootstrapServers)
      .option("subscribe", topic)
      .option("startingOffsets", "latest")
      .load()


    // Assume the message is in JSON format
    val schema = new StructType()
      .add("sensorId", StringType)
      .add("timestamp", LongType)
      .add("temperature", FloatType)
      .add("humidity", FloatType)
//    val protoDF = kafkaDF
//      .selectExpr("CAST(value AS BINARY) as value") // Extract binary Protobuf data
//      .select(from_protobuf($"value", messageType, descriptorFile).alias("sensorReading")) // Deserialize Protobuf
//      .select("sensorReading.*")

    val jsonDF = kafkaDF.selectExpr("CAST(value AS STRING) as value")
      .select(from_json($"value", schema).alias("sensorReading"))
      .select("sensorReading.*")

    val filteredDF=filterData(jsonDF)

    val query = filteredDF.writeStream
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .foreachBatch((batchDF: DataFrame, batchId: Long) => {

        if(!batchDF.isEmpty){
          val firstRow = batchDF.head()
          val timestamp = firstRow.getAs[Long]("timestamp")
          val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH")
          val formattedDate = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).format(formatter)
          println($"formattedData: ${formattedDate}")

          val rawDataPath= s"gs://$bucketName/raw/sensor-data/${formattedDate}"
          val aggrDataPath= s"gs://$bucketName/aggregated/protobuf/sensor-data/${formattedDate}"
          val jsonAggrPath= s"gs://$bucketName/aggregated/json/sensor-data/${formattedDate}"


          //saving raw Data to GCS
          saveRawDataToGCSInProtoFormat(batchDF,rawDataPath)

          val curAggrDF=getCurrAgrrDataFromGCS(aggrDataPath)

          //peform aggregation
          peformAggregationAndSaveToGCS(batchDF,curAggrDF,aggrDataPath,jsonAggrPath)
        }


      })
      .start()


    query.awaitTermination()
    println("Starting task")
    Thread.sleep(Long.MaxValue);

  }
  def saveRawDataToGCSInProtoFormat(newData: DataFrame,path:String): Unit = {
    println("saving raw data to GCS")
    val serializedNewRawData = newData.withColumn(
      "value",
      to_protobuf(struct(newData.columns.map(col): _*), rawDataMessageType, rawDataDescriptorFile)
    ).select($"value")

    // Append new raw data to GCS
    serializedNewRawData.write
      .mode(SaveMode.Append)
      .format("avro") // Save in Avro format
      .save(path)
  }
  def getCurrAgrrDataFromGCS(path: String): DataFrame = {
    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
    val aggrDataPath = new Path(path)

    if (fs.exists(aggrDataPath) && fs.listStatus(aggrDataPath).nonEmpty) {
      val deserializedAggrData = spark.read
        .format("avro")
        .option("ignoreExtension", "true")
        .load(path)
        .select(from_protobuf($"value", aggrDataMessageType, aggrDataDescriptorFile).alias("value"))
        .select("value.*")
        .cache()

//      val deserializedAggrData = aggrData
//        .select(from_protobuf($"value", aggrDataMessageType, aggrDataDescriptorFile).alias("value"))
//        .select("value.*")
      deserializedAggrData
    } else {
      spark.createDataFrame(spark.sparkContext.emptyRDD[Row], aggrSchema)
    }
  }

  def  peformAggregationAndSaveToGCS(newData: DataFrame,currAggrDF:DataFrame,path:String,jsonPath:String): Unit = {

    val newAggregatedData = newData.groupBy("sensorId")
      .agg(
        avg("temperature").alias("avgTemperature_new"),
        avg("humidity").alias("avgHumidity_new"),
        min("temperature").alias("minTemperature_new"),
        max("temperature").alias("maxTemperature_new"),
        min("humidity").alias("minHumidity_new"),
        max("humidity").alias("maxHumidity_new"),
        count("sensorId").alias("dataCount_new")
      )
    println("newaggrrData");
//    newAggregatedData.limit(10).show()
    println("currAggrDF");
//    currAggrDF.limit(10).show()

    val joinedDF =
      newAggregatedData.join(currAggrDF, Seq("sensorId"), "outer")
        .na.fill(0, Seq("avgTemperature", "avgHumidity", "minTemperature", "maxTemperature", "minHumidity", "maxHumidity", "dataCount"))
        .withColumn("avgTemperature", (col("avgTemperature_new") * col("dataCount_new") + col("avgTemperature") * col("dataCount")) / (col("dataCount_new") + col("dataCount")).cast(FloatType))
        .withColumn("avgHumidity", (col("avgHumidity_new") * col("dataCount_new") + col("avgHumidity") * col("dataCount")) / (col("dataCount_new") + col("dataCount")).cast(FloatType))
        .withColumn("minTemperature", least(col("minTemperature_new"), col("minTemperature")).cast(FloatType))
        .withColumn("maxTemperature", greatest(col("maxTemperature_new"), col("maxTemperature")).cast(FloatType))
        .withColumn("minHumidity", least(col("minHumidity_new"), col("minHumidity")).cast(FloatType))
        .withColumn("maxHumidity", greatest(col("maxHumidity_new"), col("maxHumidity")).cast(FloatType))
        .withColumn("dataCount", col("dataCount_new") + col("dataCount").cast(IntegerType))
        .select("sensorId", "avgTemperature", "avgHumidity", "minTemperature", "maxTemperature", "minHumidity", "maxHumidity", "dataCount")

    println("Printing joinedDF")
//    joinedDF.limit(10).show()
//    val serializedAggrData = joinedDF.withColumn(
//      "value",
//      to_protobuf(struct(joinedDF.columns.map(col): _*), aggrDataMessageType, aggrDataDescriptorFile)
//    ).select($"value")
val serializedAggrData = joinedDF.withColumn(
  "value",
  to_protobuf(struct(
    col("sensorId").alias("sensorId"),
    col("avgTemperature").cast(FloatType).alias("avgTemperature"),
    col("avgHumidity").cast(FloatType).alias("avgHumidity"),
    col("minTemperature").cast(FloatType).alias("minTemperature"),
    col("maxTemperature").cast(FloatType).alias("maxTemperature"),
    col("minHumidity").cast(FloatType).alias("minHumidity"),
    col("maxHumidity").cast(FloatType).alias("maxHumidity"),
    col("dataCount").cast(IntegerType).alias("dataCount")
  ), aggrDataMessageType, aggrDataDescriptorFile)
).select($"value")

    println(s"count of serializedAggrData: ${serializedAggrData.count()}")
    // Append new aggregated data to GCS
    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
    val aggrDataPath = new Path(path)
    if (!fs.exists(aggrDataPath)) {
      fs.mkdirs(aggrDataPath)
    }
    serializedAggrData.write
      .mode(SaveMode.Overwrite)
      .format("avro") // Save in Avro format
      .save(path)

    //writing in JSON format
    joinedDF.write
      .mode(SaveMode.Overwrite)
      .format("json")
      // Save in Avro format
      .save(jsonPath)
  }


}