#!/bin/bash

PKG_DIR=com/cmartin/learn
SBT_VER=1.1.0
SCALA_VER="2.12.4"
SCALATEST_VER="3.0.4"

# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}

# create project properties file
echo "sbt.version=${SBT_VER}" > project/build.properties
echo 'name := "project-template"
version := "1.0"
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