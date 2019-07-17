#!/bin/bash

PKG_DIR=com/cmartin/learn
SBT_VER=1.2.8
SCALA_VER="2.13.0"
SCALATEST_VER="3.0.8"
SCOVERAGE_VER="1.6.0"
DEP_UP_VER="1.2.1"

# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}

# create project properties file
echo "sbt.version=${SBT_VER}" > project/build.properties

# create sbt plugins file
echo 'addSbtPlugin("org.scoverage" % "sbt-scoverage" % "'${SCOVERAGE_VER}'")
addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "'${DEP_UP_VER}'")' > project/plugins.sbt

# create sbt build file
echo 'name := "project-template"
version := "1.0.0-SNAPSHOT"
scalaVersion := "'${SCALA_VER}'"

scalacOptions ++= Seq(                   // some of the Rob Norris tpolecat options
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-explaintypes",                     // Explain type errors in more detail.
    "-explaintypes",                     // Explain type errors in more detail.
    "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
    "-language:higherKinds",             // Allow higher-kinded types
    "-language:implicitConversions",
    "-language:postfixOps"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "'${SCALATEST_VER}'" % "test"' > build.sbt

# create common package
echo 'package com.cmartin

package object learn {

  trait Greeting {
    lazy val greeting: String = "simple-application-hello"
  }

}' > src/main/scala/${PKG_DIR}/package.scala


# create main application
echo 'package com.cmartin.learn

object SimpleApp extends App with Greeting {
    println(greeting)
}' > src/main/scala/${PKG_DIR}/SimpleApp.scala


echo 'package com.cmartin.learn

import org.scalatest._

class SimpleAppSpec extends FlatSpec with Matchers {
  "The SimpleApp object" should "say hello" in {
    SimpleApp.greeting shouldEqual "simple-application-hello"
  }
}' > src/test/scala/${PKG_DIR}/SimpleAppSpec.scala

sbt -batch clean coverage test coverageReport dependencyUpdates run
