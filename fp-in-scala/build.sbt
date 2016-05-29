name := "preowned-kittens"
version := "1.0"
organization := "com.cmartin"

scalaVersion := "2.11.8"
//libraryDependencies += "org.specs2" % "specs2_2.10" % "1.14" % "test"
libraryDependencies += "org.specs2" %% "specs2-core" % "3.8" % "test"
scalacOptions in Test ++= Seq("-Yrangepos")