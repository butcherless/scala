import sbt.*

object Dependencies {

  // production code
  lazy val pekkoActor = "org.apache.pekko" %% "pekko-actor" % Versions.pekko
  lazy val pekkoSlf4j = "org.apache.pekko" %% "pekko-slf4j" % Versions.pekko
  lazy val pekkoHttp  = "org.apache.pekko" %% "pekko-http"  % Versions.pekkoHttp

  lazy val pekkoActorTyped  = "org.apache.pekko" %% "pekko-actor-typed"  % Versions.pekko
  lazy val pekkoStreamTyped = "org.apache.pekko" %% "pekko-stream-typed" % Versions.pekko

  lazy val pekkoJson   = "org.apache.pekko" %% "pekko-http-spray-json" % Versions.pekkoHttp
  lazy val pekkoStream = "org.apache.pekko" %% "pekko-stream"          % Versions.pekko
  lazy val cats       = "org.typelevel"     %% "cats-core"            % Versions.cats
  lazy val catsFree   = "org.typelevel"     %% "cats-free"            % Versions.cats

  lazy val circeParser  = "io.circe" %% "circe-parser"  % Versions.circe
  lazy val circeGeneric = "io.circe" %% "circe-generic" % Versions.circe

  lazy val typesafeConfig = "com.typesafe"      % "config"           % Versions.config
  lazy val json4s         = "io.github.json4s" %% "json4s-native"    % Versions.json4s
  lazy val justSemver     = "io.kevinlee"      %% "just-semver-core" % Versions.justSemver
  lazy val kafkaClient    = "org.apache.kafka"  % "kafka-clients"    % Versions.kafkaClient
  lazy val logback        = "ch.qos.logback"    % "logback-classic"  % Versions.logback

  lazy val figlet4s = "com.colofabrix.scala" %% "figlet4s-core" % Versions.figlet4s
  lazy val refined  = "eu.timepit"           %% "refined"       % Versions.refined
  lazy val scalaz   = "org.scalaz"           %% "scalaz-core"   % Versions.scalaz

  lazy val sttpCore       = "com.softwaremill.sttp.client3" %% "core"                          % Versions.sttp
  lazy val sttpZio        = "com.softwaremill.sttp.client3" %% "zio"                           % Versions.sttp
  lazy val sttpArmeriaZio = "com.softwaremill.sttp.client3" %% "armeria-backend-zio"           % Versions.sttp
  lazy val sttpAsyncZio   = "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % Versions.sttp
  lazy val sttpZioJson    = "com.softwaremill.sttp.client3" %% "zio-json"                      % Versions.sttp

  // Z I O  a n d  E C O S Y S T E M
  lazy val zio               = "dev.zio" %% "zio"                 % Versions.zio
  lazy val zioConfig         = "dev.zio" %% "zio-config"          % Versions.zioConfig
  lazy val zioConfigTypesafe = "dev.zio" %% "zio-config-typesafe" % Versions.zioConfig
  lazy val zioLogging        = "dev.zio" %% "zio-logging-slf4j"   % Versions.zioLogging
  lazy val zioPrelude        = "dev.zio" %% "zio-prelude"         % Versions.zioPrelude

  // testing code
  lazy val pekkoHttpTest = "org.apache.pekko" %% "pekko-http-testkit"        % Versions.pekkoHttp  % Test
  lazy val pekkoTest     = "org.apache.pekko" %% "pekko-actor-testkit-typed" % Versions.pekko      % Test
  lazy val scalaTest    = "org.scalatest"     %% "scalatest"                % Versions.scalatest % Test
}
