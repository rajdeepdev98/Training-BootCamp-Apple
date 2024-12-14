ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.13"

lazy val root = (project in file("."))
  .settings(
    name := "IOT_Backend",
    idePackagePrefix := Some("org.iot.backend")
  )

//name := "Akka-GCS-Sensor-Data"
//
//version := "0.1"
//
//scalaVersion := "2.12.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.19",
  "com.typesafe.akka" %% "akka-stream" % "2.6.19",
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  "com.google.cloud" % "google-cloud-storage" % "2.7.0",  // GCS client
  "io.circe" %% "circe-core" % "0.14.1",  // JSON parsing
  "io.circe" %% "circe-parser" % "0.14.1"  // JSON parsing
)
libraryDependencies += "ch.megard" %% "akka-http-cors" % "1.0.0"



libraryDependencies += "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.7"
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.4.0"

// Resolve version conflict
// Resolve version conflict explicitly
dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"  // Explicit override