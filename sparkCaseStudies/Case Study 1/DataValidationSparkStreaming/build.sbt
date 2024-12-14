ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.13" // Use Scala 2.13 for compatibility

// Define the root project
lazy val root = (project in file("."))
  .settings(
    name := "DataValidationSparkStreaming"
  )
//PB.protocVersion := "4.26.0"

// Spark dependencies
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.4.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.4.0"  // Use 3.4.0 for consistency
libraryDependencies += "org.apache.spark" %% "spark-protobuf" % "3.4.0" // Use the correct version and match the Spark version

// ScalaPB (Protocol Buffers)
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.6"

// Google Cloud dependencies
libraryDependencies += "com.google.cloud" % "google-cloud-storage" % "2.7.0"
libraryDependencies += "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.7"

// JSON and Jackson dependencies
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.15.2"
libraryDependencies += "org.apache.spark" %% "spark-avro" % "3.4.0"
//