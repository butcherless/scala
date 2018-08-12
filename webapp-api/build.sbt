import sbt.Keys.libraryDependencies

lazy val akkaHttpVersion = "10.1.3"
lazy val akkaVersion = "2.5.14"
lazy val catsVersion = "1.2.0"
lazy val logbackVersion = "1.2.3"
lazy val scalaLoggingVersion = "3.9.0"
lazy val scalazVersion = "7.2.25"
lazy val scalatestVersion = "3.0.5"
lazy val specs2Version = "4.3.3"

lazy val projectVersion = "1.0.0-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  name := "webapi",
  version := projectVersion,
  scalaVersion := "2.12.6"
)

lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
lazy val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
lazy val catsFree = "org.typelevel" %% "cats-free" % catsVersion
lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
lazy val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion

lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
lazy val scalaTest = "org.scalatest" %% "scalatest" % scalatestVersion % "test"
lazy val specs2 = "org.specs2" %% "specs2-core" % specs2Version % "test"
lazy val uTest = "com.lihaoyi" %% "utest" % "0.6.4" % "test"

lazy val commonDep = "com.cmartin.learn" %% "common" % projectVersion
lazy val controllerDep = "com.cmartin.learn" %% "common" % projectVersion

lazy val root = (project in file(".")).aggregate(
  common,
  controller,
  repository,
  service,
  web
)

lazy val common = (project in file("common"))
  .settings(
    commonSettings,
    name := "common",
    libraryDependencies ++= Seq(scalaTest)
  )

lazy val controller = (project in file("controller"))
  .settings(
    commonSettings,
    name := "controller",
    libraryDependencies ++= Seq(commonDep)
  )

lazy val repository = (project in file("repository"))
  .settings(
    commonSettings,
    name := "repository"
  )

lazy val service = (project in file("service"))
  .settings(
    commonSettings,
    name := "service"
  )

lazy val web = (project in file("web"))
  .settings(
    commonSettings,
    name := "web",
    libraryDependencies ++= Seq(commonDep, controllerDep)
  )
