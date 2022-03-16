import sbt._

object Dependencies {

  // production code
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % Versions.akka
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % Versions.akka
  lazy val akkaHttp  = "com.typesafe.akka" %% "akka-http"  % Versions.akkaHttp

  lazy val akkaActorTyped  = "com.typesafe.akka" %% "akka-actor-typed"  % Versions.akkaTyped
  lazy val akkaStreamTyped = "com.typesafe.akka" %% "akka-stream-typed" % Versions.akkaTyped

  lazy val akkaJson   = "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttp
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream"          % Versions.akka
  lazy val cats       = "org.typelevel"     %% "cats-core"            % Versions.cats
  lazy val catsFree   = "org.typelevel"     %% "cats-free"            % Versions.cats

  lazy val circeParser  = "io.circe" %% "circe-parser"  % Versions.circe
  lazy val circeGeneric = "io.circe" %% "circe-generic" % Versions.circe

  lazy val typesafeConfig = "com.typesafe"     % "config"          % Versions.config
  lazy val json4s         = "org.json4s"      %% "json4s-native"   % Versions.json4s
  lazy val kafkaClient    = "org.apache.kafka" % "kafka-clients"   % Versions.kafkaClient
  lazy val logback        = "ch.qos.logback"   % "logback-classic" % Versions.logback // exclude("org.slf4j", "slf4j-api")
  // lazy val slf4j = "org.slf4j" % "slf4j-api" % slf4jVersion

  lazy val refined = "eu.timepit" %% "refined"     % Versions.refined
  lazy val scalaz  = "org.scalaz" %% "scalaz-core" % Versions.scalaz

  lazy val sttpCore       = "com.softwaremill.sttp.client3" %% "core"                          % Versions.sttp
  lazy val sttpZio        = "com.softwaremill.sttp.client3" %% "httpclient-backend-zio"        % Versions.sttp
  lazy val sttpArmeriaZio = "com.softwaremill.sttp.client3" %% "armeria-backend-zio"           % Versions.sttp
  lazy val sttpAsyncZio   = "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % Versions.sttp
  lazy val sttpZioJson    = "com.softwaremill.sttp.client3" %% "zio-json"                      % Versions.sttp

  // Z I O  a n d  E C O S Y S T E M
  lazy val zio               = "dev.zio" %% "zio"                 % Versions.zio
  lazy val zioConfig         = "dev.zio" %% "zio-config"          % Versions.zioConfig
  lazy val zioConfigTypesafe = "dev.zio" %% "zio-config-typesafe" % Versions.zioConfig
  lazy val zioJson           = "dev.zio" %% "zio-json"            % Versions.zioJson
  lazy val zioLogging        = "dev.zio" %% "zio-logging-slf4j"   % Versions.zioLogging
  lazy val zioPrelude        = "dev.zio" %% "zio-prelude"         % Versions.zioPrelude
  // lazy val zioStreams = "dev.zio" %% "zio-streams" % zioVersion
  // lazy val zioKafka = "dev.zio" %% "zio-kafka"  % zioKafkaVersion

  // testing code
  lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit"        % Versions.akkaHttp  % Test
  lazy val akkaTest     = "com.typesafe.akka" %% "akka-actor-testkit-typed" % Versions.akka      % Test
  lazy val pegdown      = "org.pegdown"        % "pegdown"                  % Versions.pegdown   % Test
  lazy val scalaTest    = "org.scalatest"     %% "scalatest"                % Versions.scalatest % "it,test"
}
