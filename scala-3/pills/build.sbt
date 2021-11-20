import Dependencies._
import sbtassembly.AssemblyKeys.{assembly, assemblyMergeStrategy}
import sbtassembly.MergeStrategy

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.1.0"
ThisBuild / organization := "com.cmartin.learn"

lazy val basicScalacOptions = Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-explain",
  "-explaintypes",
  "-feature",
  "-unchecked",
//  "-language:postfixOps",
  "-language:higherKinds",
  "-Wdead-code",
  "-Wunused:imports",
  "-Xlint:infer-any",
  "-Xlint:unused"
)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(scalaTest),
  scalacOptions ++= basicScalacOptions
  // resolvers += // temporal for ZIO snapshots
  //  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
)

lazy val scala3pills = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      zio,
      scalaTest
    ),
    coverageEnabled := false
  )

// clear screen and banner
lazy val cls = taskKey[Unit]("Prints a separator")
cls := {
  val brs = "\n".repeat(2)
  val message = "* B U I L D   B E G I N S   H E R E *"
  val chars = "*".repeat(message.length())
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
