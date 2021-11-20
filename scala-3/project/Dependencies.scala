import sbt._

object Dependencies {

  // production code
  lazy val logback =
    "ch.qos.logback" % "logback-classic" % Versions.logback

  // ZIO and ecosystem
  lazy val zio = "dev.zio" %% "zio" % Versions.zio

  // testing code
  lazy val scalaTest =
    "org.scalatest" %% "scalatest" % Versions.scalatest % Test
}
