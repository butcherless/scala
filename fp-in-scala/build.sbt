name := "preowned-kittens"
version := "1.0"
organization := "com.cmartin"

scalaVersion := "2.12.0"
//libraryDependencies += "org.specs2" %% "specs2-core" % "3.8" % "test"
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.8.6" % "test")
scalacOptions in Test ++= Seq("-Yrangepos")
