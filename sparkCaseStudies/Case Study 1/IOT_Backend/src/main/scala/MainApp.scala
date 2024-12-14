package org.iot.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, Materializer}
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
import org.apache.spark.sql.functions._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import akka.http.scaladsl.model.HttpMethods._
import org.apache.spark.sql.types.{FloatType, IntegerType, StringType, StructField, StructType}


object MainApp{
  implicit val system: ActorSystem = ActorSystem("AggregatedDataApi")
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val GCP_CREDENTIALS=sys.env.getOrElse("GCP_CREDENTIALS", "")


  val aggDataJsonPath="gs://de_case_study_bucket/aggregated/json/sensor-data"
  val aggrDataSchema = StructType(Array(
    StructField("sensorId", StringType, nullable = false),
    StructField("avgTemperature", FloatType, nullable = false),
    StructField("avgHumidity", FloatType, nullable = false),
    StructField("minTemperature", FloatType, nullable = false),
    StructField("maxTemperature", FloatType, nullable = false),
    StructField("minHumidity", FloatType, nullable = false),
    StructField("maxHumidity", FloatType, nullable = false),
    StructField("dataCount", IntegerType, nullable = false)
  ))
  val spark = SparkSession.builder()
    .appName("AggregatedDataApi")
    .config("spark.hadoop.fs.defaultFS", "gs://de_case_study_bucket/")
    .config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
    .config("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
    .config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
    .config("spark.hadoop.google.cloud.auth.service.account.json.keyfile", GCP_CREDENTIALS)
    .master("local[*]")
    .getOrCreate()


  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.server.Route

  val corsSettings = CorsSettings.defaultSettings
    .withAllowedOrigins(_ => true) // Allow all origins
    .withAllowedMethods(scala.collection.immutable.Seq(GET, POST, PUT, DELETE, OPTIONS)) // Allow required HTTP methods
    .withAllowCredentials(false) // If cookies/auth headers are not required, keep false
    .withExposedHeaders(scala.collection.immutable.Seq("Content-Type", "Authorization")) // Expose necessary headers

  val route: Route = cors(corsSettings) {
    options {
      complete(StatusCodes.OK) // Respond OK to preflight requests
    } ~
      concat(
        path("api" / "aggregated-data") {
          get {
            complete(HttpEntity(ContentTypes.`application/json`, fetchAllAggregatedData(spark)))
          }
        },
        path("api" / "aggregated-data" / Segment) { sensorId =>
          get {
            complete(HttpEntity(ContentTypes.`application/json`, fetchAggregatedDataBySensorId(spark, sensorId)))
          }
        }
      )
  }

  def main(args: Array[String]): Unit = {

    Http().newServerAt("localhost", 8080).bind(route).onComplete {
      case Success(binding) =>
        println(s"Server started at ${binding.localAddress}")
      case Failure(exception) =>
        println(s"Failed to bind server: ${exception.getMessage}")
        system.terminate()
    }
  }

  // Fetch aggregated data for all folders (only the latest hour folder)
  def fetchAllAggregatedData(spark: SparkSession): String = {
    println("all data request")
    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
    val latestFolderPath = getOptimizedLatestFolder(spark)


    latestFolderPath match {
      case Some(folderPath) =>
        loadFolderData(spark, folderPath)
          .toJSON
          .collect()
          .mkString("[", ",", "]")
      case None =>
        "[]" // Return empty JSON array if no folder exists
    }
  }

  // Fetch aggregated data for a specific sensorId (only the latest hour folder)
  def fetchAggregatedDataBySensorId(spark: SparkSession, sensorId: String): String = {
    import spark.implicits._

    val latestFolderPath = getOptimizedLatestFolder(spark)

    latestFolderPath match {
      case Some(folderPath) =>
        loadFolderData(spark, folderPath)
          .filter($"sensorId" === sensorId)
          .toJSON
          .collect()
          .mkString("[", ",", "]")
      case None =>
        "[]" // Return empty JSON array if no folder exists
    }
  }

  // Optimized function to get the latest folder path
  def getOptimizedLatestFolder(spark: SparkSession): Option[String] = {
    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)

    val basePathObj = new Path(aggDataJsonPath)
    if (!fs.exists(basePathObj)) return None

    // Step-by-step traversal
    val yearDirs = fs.listStatus(basePathObj).map(_.getPath)
    if (yearDirs.isEmpty) return None
    val validYearDirs = yearDirs.filter(dir => dir.getName.forall(_.isDigit))
    if (validYearDirs.isEmpty) return None
    val maxYear = validYearDirs.map(_.getName.toInt).max
//    val maxYear = yearDirs.map(_.getName.toInt).max
    println(s"maxYear $maxYear")

    val monthDirs = fs.listStatus(new Path(s"$aggDataJsonPath/$maxYear")).map(_.getPath)
    if (monthDirs.isEmpty) return None
    val maxMonth = monthDirs.map(_.getName.toInt).max
   println(s"maxMonth $maxMonth")
    val dayDirs = fs.listStatus(new Path(s"$aggDataJsonPath/$maxYear/${f"$maxMonth%02d"}")).map(_.getPath)
    if (dayDirs.isEmpty) return None
    val maxDay = dayDirs.map(_.getName.toInt).max
  println(s"maxDay $maxDay")
    val hourDirs = fs.listStatus(new Path(s"$aggDataJsonPath/$maxYear/${f"$maxMonth%02d"}/${f"$maxDay%02d"}")).map(_.getPath)
    if (hourDirs.isEmpty) return None
    val maxHour = hourDirs.map(_.getName.toInt).max
  println(s"folderpath $aggDataJsonPath/$maxYear/${f"$maxMonth%02d"}/${f"$maxDay%02d"}/${f"$maxHour%02d"}")
    Some(s"$aggDataJsonPath/$maxYear/${f"$maxMonth%02d"}/${f"$maxDay%02d"}/${f"$maxHour%02d"}")
  }

  // Load data from a specific folder
  def loadFolderData(spark: SparkSession, folderPath: String): DataFrame = {
    spark.read.format("json").schema(aggrDataSchema).load(folderPath)
  }
}
