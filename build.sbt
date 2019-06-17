lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion = "2.5.23"
lazy val catsVersion = "1.6.1"
lazy val configVersion = "1.3.4"
lazy val json4sVersion = "3.6.6"
lazy val kafkaClientVersion = "2.2.1"
lazy val logbackVersion = "1.2.3"
lazy val pegdownVersion = "1.6.0"
lazy val refinedVersion = "0.9.8"
lazy val scalaLoggingVersion = "3.9.2"
lazy val scalazVersion = "7.2.27"
lazy val scalatestVersion = "3.0.8"
lazy val specs2Version = "4.5.1"
lazy val uTestVersion = "0.7.1"

val scalaCompiler = "2.12.8"

scalaVersion := scalaCompiler

lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := scalaCompiler,
  test in assembly := {}
)

lazy val basicScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds"
)

// production code
lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
lazy val akkaJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
lazy val cats = "org.typelevel" %% "cats-core" % catsVersion
lazy val catsFree = "org.typelevel" %% "cats-free" % catsVersion
lazy val config = "com.typesafe" % "config" % configVersion
lazy val json4s = "org.json4s" %% "json4s-native" % json4sVersion
lazy val kafkaClient = "org.apache.kafka" % "kafka-clients" % kafkaClientVersion
lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
lazy val refined = "eu.timepit" %% "refined" % refinedVersion
lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
lazy val scalaz = "org.scalaz" %% "scalaz-core" % scalazVersion

// testing code
lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
lazy val akkaTest = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
lazy val pegdown = "org.pegdown" % "pegdown" % pegdownVersion % Test
lazy val scalaTest = "org.scalatest" %% "scalatest" % scalatestVersion % Test
lazy val specs2 = "org.specs2" %% "specs2-core" % specs2Version % Test
lazy val uTest = "com.lihaoyi" %% "utest" % uTestVersion % Test


lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name := "fp-in-scala",
    libraryDependencies ++= fpInScalaDeps
  )

val fpInScalaDeps = Seq(scalaz, cats, scalaTest, specs2)

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
    scalacOptions ++= basicScalacOptions
  )

lazy val typeclasses = (project in file("typeclasses"))
  .settings(
    commonSettings,
    name := "typeclasses",
    libraryDependencies ++= typeclassesDeps,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

val typeclassesDeps = Seq(
  scalaLogging, logback, uTest
)

lazy val dtogen = (project in file("dtogen"))
  .settings(
    commonSettings,
    name := "dtogen",
    libraryDependencies ++= dtogenDeps,
    testFrameworks += new TestFramework("utest.runner.Framework"),
    scalacOptions ++= basicScalacOptions
  )

val dtogenDeps = Seq(scalaLogging, logback, uTest)

lazy val scalazlearn = (project in file("scalaz"))
  .settings(
    commonSettings,
    name := "scalazlearn",
    libraryDependencies ++= scalazlearnDeps,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

val scalazlearnDeps = Seq(scalaz, uTest)

lazy val akkaActors = (project in file("akka-actors"))
  .settings(commonSettings,
    name := "akka-actors-proof-of-concept",
    libraryDependencies ++= Seq(akkaActor, logback, scalaTest)
  )

lazy val akkahttphw = (project in file("akka-http-hw"))
  .settings(commonSettings,
    name := "akka-http-webserver",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream, scalaLogging, logback, akkaHttpTest, scalaTest, akkaTest)
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

lazy val refinedPoc = (project in file("refined-poc"))
  .settings(commonSettings,
    name := "refined-proof-of-concept",
    libraryDependencies ++= Seq(refined, scalaTest)
  )

lazy val kafkaprodcons = (project in file("kafka-prod-cons"))
  .settings(commonSettings,
    name := "kafka-producer-consumer",
    libraryDependencies ++= Seq(config, kafkaClient, logback, scalaLogging, scalaTest)
  )

lazy val json4sUtils = (project in file("json4s-utils"))
  .settings(commonSettings,
    name := "json4s-utils",
    libraryDependencies ++= Seq(json4s, logback, scalaLogging, scalaTest)
  )
