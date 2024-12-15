import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{SaveMode, SparkSession}

object StaticDataToDynamic {
  val GCP_CREDENTIALS=sys.env.getOrElse("GCP_CREDENTIALS", "")
  val bucketName = "de_case_study_bucket"

  def main(args: Array[String]): Unit = {
    // Initialize SparkSession
    val spark = SparkSession.builder()
      .appName("CopyStaticTrainDataToDynamic")
      .config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
      .config("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
      .config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
      .config("spark.hadoop.google.cloud.auth.service.account.json.keyfile", GCP_CREDENTIALS)
      .master("local[*]")
      .getOrCreate()

    val updatedTrainPath = s"gs://$bucketName/final_project/case_study_4/updated_train.csv"

    val initialTrainPath = s"gs://$bucketName/final_project/case_study_4/train.csv"
    val initialTrainDF = spark.read
      .option("header", "true") // Adjusted for datasets saved with headers
      .option("inferSchema", "true")
      .csv(initialTrainPath)
    println(initialTrainDF.count())

    initialTrainDF.write.mode(SaveMode.Overwrite).option("header", "true").csv(updatedTrainPath)



    spark.stop()
  }
}