ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "RealTimeIOT",
    idePackagePrefix := Some("org.dataeng.cs1")
  )
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.20",
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.1.1",
  "org.apache.kafka" % "kafka-clients" % "3.0.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.20"
)
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"

