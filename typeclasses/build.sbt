name := "typeclasses"
version := "1.0"
organization := "com.cmartin"
scalaVersion := "2.12.4"

logLevel := Level.Info

libraryDependencies += "com.lihaoyi" %% "utest" % "0.6.0" % "test"

testFrameworks += new TestFramework("utest.runner.Framework")