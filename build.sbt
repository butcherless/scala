import Dependencies._

val scalaCompiler = "2.13.0"

scalaVersion := scalaCompiler

lazy val basicScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds"
)



lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := scalaCompiler,
  libraryDependencies ++= Seq(scalaTest),
  scalacOptions ++= basicScalacOptions,
  test in assembly := {}
)


lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name := "fp-in-scala",
    libraryDependencies ++= Seq(scalaz, cats, specs2)
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
    libraryDependencies ++= Seq(cats, catsFree, scalaz),
  )

lazy val typeclasses = (project in file("typeclasses"))
  .settings(
    commonSettings,
    name := "typeclasses",
    libraryDependencies ++= Seq(scalaLogging, logback),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

lazy val dtogen = (project in file("dtogen"))
  .settings(
    commonSettings,
    name := "dtogen",
    libraryDependencies ++= Seq(scalaLogging, logback),
    testFrameworks += new TestFramework("utest.runner.Framework"),
  )

lazy val scalazlearn = (project in file("scalaz"))
  .settings(
    commonSettings,
    name := "scalazlearn",
    libraryDependencies ++= Seq(scalaz, uTest),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

lazy val akkaActors = (project in file("akka-actors"))
  .settings(commonSettings,
    name := "akka-actors-proof-of-concept",
    libraryDependencies ++= Seq(akkaActor, akkaStream, logback),
  )

lazy val akkaActorsTyped = (project in file("akka-actors-typed"))
  .settings(commonSettings,
    name := "akka-actors-typed-poc",
    libraryDependencies ++= Seq(akkaHttp, akkaActorTyped, akkaStreamTyped, akkaSlf4j, logback, akkaTest),
  )

lazy val akkahttphw = (project in file("akka-http-hw"))
  .settings(commonSettings,
    name := "akka-http-webserver",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream, scalaLogging, logback, akkaHttpTest, akkaTest)
  )

lazy val akkahttpcl = (project in file("akka-http-cl"))
  .settings(commonSettings,
    name := "akka-http-webclient",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream)
  )

lazy val calendar = (project in file("calendar"))
  .settings(commonSettings,
    name := "personal-calendar",
    libraryDependencies ++= Seq(scalaz, specs2)
  )

lazy val refinedPoc = (project in file("refined-poc"))
  .settings(commonSettings,
    name := "refined-proof-of-concept",
    libraryDependencies ++= Seq(refined)
  )

lazy val kafkaprodcons = (project in file("kafka-prod-cons"))
  .settings(commonSettings,
    name := "kafka-producer-consumer",
    libraryDependencies ++= Seq(typesafeConfig, kafkaClient, logback, scalaLogging)
  )

lazy val json4sUtils = (project in file("json4s-utils"))
  .settings(commonSettings,
    name := "json4s-utils",
    libraryDependencies ++= Seq(json4s, logback, scalaLogging)
  )

