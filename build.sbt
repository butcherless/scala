lazy val akkaHttpVersion = "10.1.1"
lazy val akkaVersion = "2.5.12"
lazy val catsVersion = "1.1.0"
lazy val logbackVersion = "1.2.3"
lazy val scalazVersion = "7.2.23"

lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.12.6"
)

lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
lazy val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
val cats = "org.typelevel" %% "cats-core" % catsVersion
val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion
val scala_logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
val specs2 = "org.specs2" %% "specs2-core" % "4.2.0" % "test"
val uTest = "com.lihaoyi" %% "utest" % "0.6.4" % "test"

val zinc = "org.scala-sbt" % "zinc_2.12" % "1.1.7"


lazy val root = (project in file(".")).aggregate(
  akkahttpcl,
  akkahttphw,
  calendar,
  depAnalyzer,
  dtogen,
  fpInScala,
  hkTypes,
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

lazy val hkTypes = (project in file("hk-types"))
  .settings(
    commonSettings,
    name := "hkTypes",
    libraryDependencies ++= Seq(scalaTest, scalaz),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:higherKinds")
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
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream, scalaTest)
  )

lazy val akkahttpcl = (project in file("akka-http-cl"))
  .settings(commonSettings,
    name := "akka-http-webclient",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream, scalaTest)
  )

lazy val calendar = (project in file("calendar"))
  .settings(commonSettings,
    name := "personal-calendar",
    libraryDependencies ++= Seq(scalaz, scalaTest, specs2)
  )
