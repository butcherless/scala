lazy val commonSettings = Seq(
  organization := "com.cmartin.learn",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.4"
)

val uTest = "com.lihaoyi" %% "utest" % "0.6.0" % "test"
val scala_logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaz = "org.scalaz" %% "scalaz-core" % "7.2.17"
val cats = "org.typelevel" %% "cats-core" % "1.0.0-MF"
val specs2 = "org.specs2" %% "specs2-core" % "3.8.6" % "test"
val zinc = "org.scala-sbt" % "zinc_2.12" % "1.0.3"


lazy val root = (project in file(".")).aggregate(fpInScala, typeclasses, dtogen, scalazlearn)

lazy val fpInScala = (project in file("fp-in-scala"))
  .settings(
    commonSettings,
    name := "fp-in-scala",
    libraryDependencies ++= fpInScalaDeps
  )

val fpInScalaDeps = Seq(
  zinc, scalaz, cats, specs2
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
    testFrameworks += new TestFramework("utest.runner.Framework")
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
