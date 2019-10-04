import sbt._

object Dependencies {
  lazy val akkaHttpVersion = "10.1.10"
  lazy val akkaVersion = "2.5.25"
  lazy val akkaTypedVersion = "2.5.25"
  lazy val catsVersion = "2.0.0"
  lazy val circeVersion = "0.12.1"
  lazy val configVersion = "1.3.4"
  lazy val json4sVersion = "3.6.7"
  lazy val kafkaClientVersion = "2.3.0"
  lazy val logbackVersion = "1.2.3"
  lazy val pegdownVersion = "1.6.0"
  lazy val refinedVersion = "0.9.10"
  lazy val scalaLoggingVersion = "3.9.2"
  lazy val scalazVersion = "7.2.28"
  lazy val scalatestVersion = "3.0.8"
  lazy val slf4jVersion = "1.7.26"
  lazy val specs2Version = "4.7.1"
  lazy val sttpVersion = "1.7.0"
  lazy val uTestVersion = "0.7.1"
  lazy val zioVersion = "1.0.0-RC13"

  // production code
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion

  lazy val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaTypedVersion
  lazy val akkaStreamTyped = "com.typesafe.akka" %% "akka-stream-typed" % akkaTypedVersion

  lazy val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
  lazy val catsFree = "org.typelevel" %% "cats-free" % catsVersion

  lazy val circeParser = "io.circe" %% "circe-parser" % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

  lazy val typesafeConfig = "com.typesafe" % "config" % configVersion
  lazy val json4s = "org.json4s" %% "json4s-native" % json4sVersion
  lazy val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaClientVersion
  lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion //exclude("org.slf4j", "slf4j-api")
  //lazy val slf4j = "org.slf4j" % "slf4j-api" % slf4jVersion

  lazy val refined = "eu.timepit" %% "refined" % refinedVersion
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  lazy val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion
  lazy val zinc = "org.scala-sbt" %% "zinc" % "1.3.0-M6"

  lazy val sttpBackend = "com.softwaremill.sttp" %% "akka-http-backend" % sttpVersion
  lazy val sttpJson4s = "com.softwaremill.sttp" %% "json4s" % sttpVersion

  lazy val zio = "dev.zio" %% "zio" % zioVersion

  // testing code
  lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
  lazy val akkaTest = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test
  lazy val pegdown = "org.pegdown" % "pegdown" % pegdownVersion % Test
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalatestVersion % Test
  lazy val specs2 = "org.specs2" %% "specs2-core" % specs2Version % Test
  lazy val uTest = "com.lihaoyi" %% "utest" % uTestVersion % Test

}
