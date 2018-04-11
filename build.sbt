lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "0.1.2-SNAPSHOT",
  scalaVersion := "2.12.5"
)

lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.11"
lazy val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11"
val cats = "org.typelevel" %% "cats-core" % "1.0.1"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scala_logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
val scalaz = "org.scalaz" %% "scalaz-core" % "7.2.20"

val specs2 = "org.specs2" %% "specs2-core" % "4.0.3" % "test"
val uTest = "com.lihaoyi" %% "utest" % "0.6.3" % "test"
lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

val zinc = "org.scala-sbt" % "zinc_2.12" % "1.1.1"


lazy val root = (project in file(".")).aggregate(
  akkahttpcl,
  akkahttphw,
  calendar,
  depAnalyzer,
  dtogen,
  fpInScala,
  scalazlearn,
  typeclasses
)

lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name := "fp-in-scala",
    libraryDependencies ++= fpInScalaDeps
  )

val fpInScalaDeps = Seq(
  zinc, scalaz, cats, specs2
)


lazy val depAnalyzer = (project in file("dep-analyzer"))
  .settings(
    commonSettings,
    name := "depAnalyzer",
    libraryDependencies ++= Seq(specs2)
  )

lazy val typeclasses = (project in file("typeclasses"))
  .settings(
    commonSettings,
    name := "typeclasses",
    libraryDependencies ++= typeclassesDeps,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

val typeclassesDeps = Seq(
  zinc, scala_logging, logback, uTest
)

lazy val dtogen = (project in file("dtogen"))
  .settings(
    commonSettings,
    name := "dtogen",
    libraryDependencies ++= dtogenDeps,
    testFrameworks += new TestFramework("utest.runner.Framework"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )

val dtogenDeps = Seq(
  zinc, scala_logging, logback, uTest
)

lazy val scalazlearn = (project in file("scalaz"))
  .settings(
    commonSettings,
    name := "scalazlearn",
    libraryDependencies ++= scalazlearnDeps,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

val scalazlearnDeps = Seq(
  scalaz, uTest
)

lazy val akkahttphw = (project in file("akka-http-hw"))
  .settings(commonSettings,
    name := "akka-http-webserver",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, scalaTest)
  )

lazy val akkahttpcl = (project in file("akka-http-cl"))
  .settings(commonSettings,
    name := "akka-http-webclient",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, scalaTest)
  )

lazy val calendar = (project in file("calendar"))
  .settings(commonSettings,
    name := "personal-calendar",
    libraryDependencies ++= Seq(scalaz, scalaTest, specs2)
  )