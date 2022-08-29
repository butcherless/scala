import Dependencies._
import sbtassembly.AssemblyKeys.{assembly, assemblyMergeStrategy}
import sbtassembly.MergeStrategy

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / organization := "com.cmartin.learn"

lazy val basicScalacOptions = Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Xlint:unused"
)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(scalaTest),
  scalacOptions ++= basicScalacOptions
  // resolvers += // temporal for ZIO snapshots
  //  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
)

lazy val common = (project in file("common"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    Defaults.itSettings,
    name := "common",
    libraryDependencies ++= Seq(logback, zio, scalaTest)
  )

lazy val fpInScala = (project in file("fp-in-scala"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name    := "fp-in-scala",
    version := "0.1.0",
    libraryDependencies ++= Seq(scalaz, cats)
  )

lazy val `zio-prelude` = (project in file("zio-prelude"))
  .configs(IntegrationTest)
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "zio-prelude",
    libraryDependencies ++= Seq(zioPrelude, scalaTest)
  )

lazy val depAnalyzer = (project in file("dep-analyzer"))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    parallelExecution          := false,
    commonSettings,
    name                       := "dependency-lookout-app",
    version                    := "1.0.0",
    libraryDependencies ++= Seq(
      figlet4s,
      akkaStream,
      json4s,
      sttpCore,
      sttpZio,
      sttpZioJson,
      zio,
      zioConfig,
      zioConfigTypesafe,
      zioLogging
    ),
    assemblyMergeStrategy      := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.discard
      case x                                       =>
        val oldStrategy = assemblyMergeStrategy.value
        oldStrategy(x)
    },
    assembly / mainClass       := Some("com.cmartin.utils.DependencyLookoutApp"),
    assembly / assemblyJarName := "depLookoutApp.jar",
    dockerBaseImage            := "eclipse-temurin:17-jdk"
  )
  .dependsOn(common)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)

lazy val hkTypes = (project in file("hk-types"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "hkTypes",
    libraryDependencies ++= Seq(cats, catsFree, scalaz)
  )

lazy val typeclasses = (project in file("typeclasses"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "typeclasses",
    libraryDependencies ++= Seq(logback)
  )

lazy val dtogen = (project in file("dtogen"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "dtogen",
    libraryDependencies ++= Seq(logback)
  )

lazy val scalazlearn = (project in file("scalaz"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "scalazlearn",
    libraryDependencies ++= Seq(scalaz)
  )

lazy val akkaActors = (project in file("akka-actors"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "akka-actors-proof-of-concept",
    libraryDependencies ++= Seq(akkaActor, akkaStream, logback)
  )

lazy val akkaActorsTyped = (project in file("akka-actors-typed"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "akka-actors-typed-poc",
    libraryDependencies ++= Seq(
      akkaHttp,
      akkaActorTyped,
      akkaStreamTyped,
      logback,
      zio,
      akkaTest
    )
  )

lazy val akkahttphw = (project in file("akka-http-hw"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "akka-http-webserver",
    libraryDependencies ++= Seq(
      akkaHttp,
      akkaJson,
      akkaStream,
      logback,
      akkaHttpTest,
      akkaTest
    )
  )

lazy val akkahttpcl = (project in file("akka-http-cl"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "akka-http-webclient",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream)
  )

lazy val calendar = (project in file("calendar"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "personal-calendar",
    libraryDependencies ++= Seq(scalaz)
  )

lazy val refinedPoc = (project in file("refined-poc"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "refined-proof-of-concept",
    libraryDependencies ++= Seq(refined)
  )

lazy val kafkaprodcons = (project in file("kafka-prod-cons"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "kafka-producer-consumer",
    libraryDependencies ++= Seq(
      circeParser,
      circeGeneric,
      typesafeConfig,
      kafkaClient,
      logback
    )
  )
  .dependsOn(common)

lazy val json4sUtils = (project in file("json4s-utils"))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    name := "json4s-utils",
    libraryDependencies ++= Seq(json4s, logback)
  )

// clear screen and banner
lazy val cls = taskKey[Unit]("Prints a separator")
cls := {
  val brs     = "\n".repeat(2)
  val message = "* B U I L D   B E G I N S   H E R E *"
  val chars   = "*".repeat(message.length())
  println(s"$brs$chars")
  println("* B U I L D   B E G I N S   H E R E *")
  println(s"$chars$brs ")
}

addCommandAlias("xcoverage", "clean;coverage;test;coverageReport")
addCommandAlias("xreload", "clean;reload")
addCommandAlias("xstart", "clean;reStart")
addCommandAlias("xstop", "reStop;clean")
addCommandAlias("xupdate", "clean;update")
addCommandAlias("xdup", "dependencyUpdates")
