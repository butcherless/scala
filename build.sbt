lazy val akkaHttpVersion = "10.1.3"
lazy val akkaVersion = "2.5.14"
lazy val catsVersion = "1.2.0"
lazy val logbackVersion = "1.2.3"
lazy val scalaLoggingVersion = "3.9.0"
lazy val scalazVersion = "7.2.26"
lazy val scalatestVersion = "3.0.5"
lazy val specs2Version = "4.3.3"

lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "1.0.0-SNAPSHOT",
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

lazy val zinc = "org.scala-sbt" % "zinc_2.12" % "1.1.7"



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
    libraryDependencies ++= Seq(cats, catsFree, scalaz, scalaTest),
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
  zinc, scalaLogging, logback, uTest
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
  zinc, scalaLogging, logback, uTest
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
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream, scalaLogging, logback, akkaHttpTest, scalaTest)
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
