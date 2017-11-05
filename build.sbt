lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.4"
)

val uTest = "com.lihaoyi" %% "utest" % "0.6.0" % "test"
val scala_logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaz = "org.scalaz" %% "scalaz-core" % "7.2.16"
val cats = "org.typelevel" %% "cats-core" % "1.0.0-MF"
val specs2 = "org.specs2" %% "specs2-core" % "3.8.6" % "test"



//testFrameworks += new TestFramework("utest.runner.Framework")

//lazy val root = (project in file("."))  .aggregate(subprj_one)

lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name := "fp-in-scala",
    libraryDependencies ++= Seq(
      scalaz,
      cats,
      specs2
    )
  )

lazy val typeclasses = (project in file("typeclasses"))
  .settings(
    commonSettings,
    name := "typeclasses",
    libraryDependencies ++= Seq(
      uTest
    )
  )
