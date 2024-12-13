
//import org.apache.spark.streaming.kafka010._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.protobuf.functions.from_protobuf
import org.apache.spark.sql.functions._

object SensorDataStreamingJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("SensorDataStreamer")
      .master("local[*]") // Local mode for testing
      .getOrCreate()
    import spark.implicits._
//
//
//    val kafkaBootstrapServers = "localhost:9092"
//    val topic = "sensor-readings"
//    val descriptorFile = "src/main/desccriptor/sensor_reading.desc"
//
//    //protobuf message type -> fully qualified
//    val messageType = "SensorReading.SensorReading"
//
//
//    val kafkaDF = spark.readStream
//      .format("kafka")
//      .option("kafka.bootstrap.servers", kafkaBootstrapServers)
//      .option("subscribe", topic)
//      .option("startingOffsets", "earliest")
//      .load()
//
//    val protoDF = kafkaDF
//      .selectExpr("CAST(value AS BINARY) as value") // Extract binary Protobuf data
//      .select(from_protobuf($"value", messageType, descriptorFile).alias("sensorReading")) // Deserialize Protobuf
//      .select("sensorReading.*")
//
//    val query = protoDF.writeStream
//      .outputMode("append")
//      .format("console")
//      .start()
//
//    query.awaitTermination()
    println("Starting task")
    Thread.sleep(Long.MaxValue);

  }
}