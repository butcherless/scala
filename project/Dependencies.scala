import sbt._

object Dependencies {
  lazy val akkaHttpVersion     = "10.1.12"
  lazy val akkaVersion         = "2.6.7"
  lazy val akkaTypedVersion    = "2.6.7"
  lazy val catsVersion         = "2.1.1"
  lazy val circeVersion        = "0.13.0"
  lazy val configVersion       = "1.4.0"
  lazy val json4sVersion       = "3.6.9"
  lazy val kafkaClientVersion  = "2.5.0"
  lazy val logbackVersion      = "1.2.3"
  lazy val pegdownVersion      = "1.6.0"
  lazy val refinedVersion      = "0.9.14"
  lazy val scalaLoggingVersion = "3.9.2"
  lazy val scalazVersion       = "7.3.2"
  lazy val scalatestVersion    = "3.2.0"
  lazy val slf4jVersion        = "1.7.26"
  lazy val specs2Version       = "4.10.0"
  lazy val sttpVersion         = "2.2.1"
  lazy val zioVersion          = "1.0.0-RC21-2"
  lazy val zioConfigVersion    = "1.0.0-RC23-1"
  lazy val zioKafkaVersion     = "0.11.0"

  // production code
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  lazy val akkaHttp  = "com.typesafe.akka" %% "akka-http"  % akkaHttpVersion

  lazy val akkaActorTyped  = "com.typesafe.akka" %% "akka-actor-typed"  % akkaTypedVersion
  lazy val akkaStreamTyped = "com.typesafe.akka" %% "akka-stream-typed" % akkaTypedVersion

  lazy val akkaJson   = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream"          % akkaVersion
  lazy val cats       = "org.typelevel"     %% "cats-core"            % catsVersion
  lazy val catsFree   = "org.typelevel"     %% "cats-free"            % catsVersion

  lazy val circeParser  = "io.circe" %% "circe-parser"  % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

  lazy val typesafeConfig = "com.typesafe"     % "config"          % configVersion
  lazy val json4s         = "org.json4s"       %% "json4s-native"  % json4sVersion
  lazy val kafkaClient    = "org.apache.kafka" % "kafka-clients"   % kafkaClientVersion
  lazy val logback        = "ch.qos.logback"   % "logback-classic" % logbackVersion //exclude("org.slf4j", "slf4j-api")
  //lazy val slf4j = "org.slf4j" % "slf4j-api" % slf4jVersion

  lazy val refined      = "eu.timepit"                 %% "refined"       % refinedVersion
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  lazy val scalaz       = "org.scalaz"                 %% "scalaz-core"   % scalazVersion
  lazy val zinc         = "org.scala-sbt"              %% "zinc"          % "1.3.0-M6"

  lazy val sttpCore = "com.softwaremill.sttp.client" %% "core"                          % sttpVersion
  lazy val sttpZio  = "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpVersion

  // Z I O  a n d  E C O S Y S T E M
  lazy val zio       = "dev.zio" %% "zio"        % zioVersion
  lazy val zioConfig = "dev.zio" %% "zio-config" % zioConfigVersion
  //lazy val zioStreams = "dev.zio" %% "zio-streams" % zioVersion
  //lazy val zioKafka = "dev.zio" %% "zio-kafka"  % zioKafkaVersion

  // testing code
  lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion  % Test
  lazy val akkaTest     = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion      % Test
  lazy val pegdown      = "org.pegdown"       % "pegdown"                   % pegdownVersion   % Test
  lazy val scalaTest    = "org.scalatest"     %% "scalatest"                % scalatestVersion % Test
  lazy val specs2       = "org.specs2"        %% "specs2-core"              % specs2Version    % Test
}
