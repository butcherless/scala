import Dependencies._
import sbtassembly.AssemblyKeys.{assembly, assemblyMergeStrategy}
import sbtassembly.MergeStrategy

ThisBuild / scalaVersion := "2.13.6"
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
  .settings(
    commonSettings,
    Defaults.itSettings,
    name := "common",
    libraryDependencies ++= Seq(logback, scalaTest)
  )

lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name := "fp-in-scala",
    libraryDependencies ++= Seq(scalaz, cats)
  )

lazy val `zio-prelude` = (project in file("zio-prelude"))
  .settings(
    commonSettings,
    name := "zio-prelude",
    libraryDependencies ++= Seq(zioPrelude, scalaTest)
  )

lazy val depAnalyzer = (project in file("dep-analyzer"))
  .settings(
    commonSettings,
    name := "depAnalyzer",
    libraryDependencies ++= Seq(
      akkaStream,
      circeGeneric,
      circeParser,
      json4s,
      sttpCore,
      sttpZio,
      logback,
      zio,
      zioConfig
    ),
    assemblyMergeStrategy := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.discard
      case x =>
        val oldStrategy = assemblyMergeStrategy.value
        oldStrategy(x)
    }
  )
  .dependsOn(common)

lazy val hkTypes = (project in file("hk-types"))
  .settings(
    commonSettings,
    name := "hkTypes",
    libraryDependencies ++= Seq(cats, catsFree, scalaz)
  )

lazy val typeclasses = (project in file("typeclasses"))
  .settings(
    commonSettings,
    name := "typeclasses",
    libraryDependencies ++= Seq(scalaLogging, logback)
  )

lazy val dtogen = (project in file("dtogen"))
  .settings(
    commonSettings,
    name := "dtogen",
    libraryDependencies ++= Seq(scalaLogging, logback)
  )

lazy val scalazlearn = (project in file("scalaz"))
  .settings(
    commonSettings,
    name := "scalazlearn",
    libraryDependencies ++= Seq(scalaz)
  )

lazy val akkaActors = (project in file("akka-actors"))
  .settings(
    commonSettings,
    name := "akka-actors-proof-of-concept",
    libraryDependencies ++= Seq(akkaActor, akkaStream, logback)
  )

lazy val akkaActorsTyped = (project in file("akka-actors-typed"))
  .settings(
    commonSettings,
    name := "akka-actors-typed-poc",
    libraryDependencies ++= Seq(akkaHttp, akkaActorTyped, akkaStreamTyped, logback, zio, akkaTest)
  )

lazy val akkahttphw = (project in file("akka-http-hw"))
  .settings(
    commonSettings,
    name := "akka-http-webserver",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream, scalaLogging, logback, akkaHttpTest, akkaTest)
  )

lazy val akkahttpcl = (project in file("akka-http-cl"))
  .settings(
    commonSettings,
    name := "akka-http-webclient",
    libraryDependencies ++= Seq(akkaHttp, akkaJson, akkaStream)
  )

lazy val calendar = (project in file("calendar"))
  .settings(
    commonSettings,
    name := "personal-calendar",
    libraryDependencies ++= Seq(scalaz)
  )

lazy val refinedPoc = (project in file("refined-poc"))
  .settings(
    commonSettings,
    name := "refined-proof-of-concept",
    libraryDependencies ++= Seq(refined)
  )

lazy val kafkaprodcons = (project in file("kafka-prod-cons"))
  .settings(
    commonSettings,
    name := "kafka-producer-consumer",
    libraryDependencies ++= Seq(
      circeParser,
      circeGeneric,
      typesafeConfig,
      kafkaClient,
      logback,
      scalaLogging
    )
  )
  .dependsOn(common)

lazy val json4sUtils = (project in file("json4s-utils"))
  .settings(
    commonSettings,
    name := "json4s-utils",
    libraryDependencies ++= Seq(json4s, logback, scalaLogging)
  )

lazy val scala3pills = (project in file("scala3-pills"))
  .settings(
    scalaVersion := "3.0.1",
    libraryDependencies ++= Seq(
      zio,
      scalaTest
    ),
    coverageEnabled := false
  )

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("xcoverage", "clean;coverage;test;coverageReport")
addCommandAlias("xreload", "clean;reload")
addCommandAlias("xstart", "clean;reStart")
addCommandAlias("xstop", "reStop;clean")
addCommandAlias("xupdate", "clean;update")
addCommandAlias("xdup", "dependencyUpdates")
