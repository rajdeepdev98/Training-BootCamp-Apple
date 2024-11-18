import scala.collection.immutable.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "NotificationService",
    idePackagePrefix := Some("org.trainingapp.notificationservice")
  )

resolvers += "Akka library repository".at("https://repo.akka.io/maven")
//lazy val akkaVersion = sys.props.getOrElse("akka.version", "2.9.3")
lazy val akkaVersion = "2.9.3"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.13",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % "6.0.0",
  "com.typesafe.akka" %% "akka-http" % "10.6.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.6.3",
  "org.apache.kafka" %% "kafka" % "3.7.0" // Kafka client
)
libraryDependencies += "com.typesafe" % "config" % "1.4.2" //accessing config files
