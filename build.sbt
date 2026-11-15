import Dependencies.*
import sbtassembly.AssemblyKeys.{assembly, assemblyMergeStrategy}
import sbtassembly.MergeStrategy

scalaVersion := Versions.scala
organization := "com.cmartin.learn"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val basicScalacOptions = Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-feature",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Wunused:imports,privates"
)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(scalaTest),
  scalacOptions ++= basicScalacOptions,
  Compile / scalacOptions ++= {
    if (coverageEnabled.value)
      Seq(s"-Xmacro-settings:scoverage-cache-buster=${System.nanoTime()}")
    else Seq.empty
  }
  // resolvers += // temporal for ZIO snapshots
  //  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
)

lazy val common = (project in file("common"))
  .settings(
    commonSettings,
    name := "common",
    libraryDependencies ++= Seq(logback, zio, scalaTest)
  )

lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name    := "fp-in-scala",
    version := "0.1.0",
    libraryDependencies ++= Seq(scalaz, cats)
  )

lazy val `zio-prelude` = (project in file("zio-prelude"))
  .settings(
    commonSettings,
    name := "zio-prelude",
    libraryDependencies ++= Seq(zioPrelude, scalaTest)
  )

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
    libraryDependencies ++= Seq(logback)
  )

lazy val pekkoActors = (project in file("pekko-actors"))
  .settings(
    commonSettings,
    name := "pekko-actors-proof-of-concept",
    libraryDependencies ++= Seq(pekkoActor, pekkoStream, logback)
  )

lazy val pekkoActorsTyped = (project in file("pekko-actors-typed"))
  .settings(
    commonSettings,
    name := "pekko-actors-typed-poc",
    libraryDependencies ++= Seq(
      pekkoHttp,
      pekkoActorTyped,
      pekkoStreamTyped,
      logback,
      zio,
      pekkoTest
    )
  )

lazy val pekkohttphw = (project in file("pekko-http-hw"))
  .settings(
    commonSettings,
    name := "pekko-http-webserver",
    libraryDependencies ++= Seq(
      pekkoHttp,
      pekkoJson,
      pekkoStream,
      logback,
      pekkoHttpTest,
      pekkoTest
    )
  )

lazy val pekkohttpcl = (project in file("pekko-http-cl"))
  .settings(
    commonSettings,
    name := "pekko-http-webclient",
    libraryDependencies ++= Seq(pekkoHttp, pekkoJson, pekkoStream)
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
      logback
    )
  )
  .dependsOn(common)

lazy val json4sUtils = (project in file("json4s-utils"))
  .settings(
    commonSettings,
    name := "json4s-utils",
    libraryDependencies ++= Seq(json4s, logback),
    // json4s extract[T] requires Manifest synthesis, no Scala 3 alternative
    scalacOptions += "-Wconf:msg=Compiler synthesis of Manifest:s"
  )

// clear screen and banner
lazy val cls = taskKey[Unit]("Prints a separator")
LocalRootProject / cls := Def.uncached {
  val brs           = "\n".repeat(2)
  val message       = "BUILD BEGINS HERE"
  val spacedMessage = message.mkString("* ", " ", " *")
  val chars         = "*".repeat(spacedMessage.length())
  println(s"$brs$chars")
  println(spacedMessage)
  println(s"$chars$brs ")
}

lazy val prepareCoverageDirectories = taskKey[Unit]("Creates scoverage data directories")
LocalRootProject / prepareCoverageDirectories := Def.uncached {
  IO.createDirectory(coverageDataDir.value / "scoverage-data")
}

addCommandAlias(
  "xcoverage",
  "coverage;prepareCoverageDirectories;testFull;coverageReport;coverageOff"
)
addCommandAlias("xreload", "clean;reload")
addCommandAlias("xupdate", "clean;update")
addCommandAlias("xdup", "dependencyUpdates")

assemblyMergeStrategy := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.discard
  case "module-info.class"                     => MergeStrategy.discard
  case x                                       =>
    val oldStrategy = assemblyMergeStrategy.value
    oldStrategy(x)
}
