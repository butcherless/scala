#!/bin/bash

PKG_DIR=com/cmartin/learn
SBT_VER=1.1.4
SCALA_VER="2.12.5"
SCALATEST_VER="3.0.5"
SCOVERAGE_VER="1.5.1"

# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}

# create project properties file
echo "sbt.version=${SBT_VER}" > project/build.properties

# create sbt plugins file
echo 'addSbtPlugin("org.scoverage" % "sbt-scoverage" % "'${SCOVERAGE_VER}'")' > project/plugins.sbt

# create sbt build file
echo 'name := "project-template"
version := "1.0.0-SNAPSHOT"
scalaVersion := "'${SCALA_VER}'"

libraryDependencies += "org.scalatest" %% "scalatest" % "'${SCALATEST_VER}'" % "test"' > build.sbt

echo 'package com.cmartin.learn

trait Greeting {
  lazy val greeting: String = "simple-application-hello"
}

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

sbt test run
