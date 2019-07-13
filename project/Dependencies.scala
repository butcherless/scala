import sbt._

object Dependencies {
  lazy val akkaHttpVersion = "10.1.8"
  lazy val akkaVersion = "2.5.23"
  lazy val catsVersion = "2.0.0-M4"
  lazy val configVersion = "1.3.4"
  lazy val json4sVersion = "3.6.7"
  lazy val kafkaClientVersion = "2.3.0"
  lazy val logbackVersion = "1.2.3"
  lazy val pegdownVersion = "1.6.0"
  lazy val refinedVersion = "0.9.8"
  lazy val scalaLoggingVersion = "3.9.2"
  lazy val scalazVersion = "7.2.28"
  lazy val scalatestVersion = "3.0.8"
  lazy val specs2Version = "4.6.0"
  lazy val uTestVersion = "0.7.1"

  // production code
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  lazy val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
  lazy val catsFree = "org.typelevel" %% "cats-free" % catsVersion
  lazy val typesafeConfig = "com.typesafe" % "config" % configVersion
  lazy val json4s = "org.json4s" %% "json4s-native" % json4sVersion
  lazy val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaClientVersion
  lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  lazy val refined = "eu.timepit" %% "refined" % refinedVersion
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  lazy val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion
  lazy val zinc = "org.scala-sbt" %% "zinc" % "1.3.0-M6"


  // testing code
  lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
  lazy val akkaTest = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  lazy val pegdown = "org.pegdown" % "pegdown" % pegdownVersion % Test
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalatestVersion % Test
  lazy val specs2 = "org.specs2" %% "specs2-core" % specs2Version % Test
  lazy val uTest = "com.lihaoyi" %% "utest" % uTestVersion % Test
  
}
