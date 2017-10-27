name := "preowned-kittens"
version := "1.0"
organization := "com.cmartin"

scalaVersion := "2.12.4"
//libraryDependencies += "org.specs2" %% "specs2-core" % "3.8" % "test"
libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.16",
  "org.typelevel" %% "cats-core" % "1.0.0-MF",
  "org.specs2" %% "specs2-core" % "3.8.6" % "test"
)
scalacOptions in Test ++= Seq("-Yrangepos")
