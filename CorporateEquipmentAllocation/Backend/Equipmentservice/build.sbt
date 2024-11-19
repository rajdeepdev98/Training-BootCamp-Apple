name := """Equipmentservice"""
organization:="com.project"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

// Example sbt version setting




// Make sure Play Plugin is enabled in project/plugins.sbt (if applicable)
//addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.0")


libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.5" // Use the appropriate version
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.1.0"
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.1.0"


libraryDependencies ++= Seq(

  "mysql" % "mysql-connector-java" % "8.0.26",
  "org.playframework" %% "play-slick"% "6.1.1",
  "org.playframework" %% "play-slick-evolutions" % "6.1.1",

)

//"com.typesafe.play" %% "play-slick" % "4.0.0",
//"com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",

libraryDependencies += ws
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.0.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"


//