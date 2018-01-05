#!/bin/bash

PKG_DIR=com/cmartin/learn
# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}

# create project properties file
echo 'sbt.version=1.0.4' > project/build.properties
echo 'name := "project-template"
version := "1.0"
scalaVersion := "2.12.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"' > build.sbt

echo 'package com.cmartin.learn

trait Greeting {
  lazy val greeting: String = "simple-application-hello"
}

object SimpleApp extends App with Greeting {
    println(greeting)
}' > src/main/scala/${PKG_DIR}/SimpleApp.scala


echo 'package com.cmartin.learn

import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {
  "The SimpleApp object" should "say hello" in {
    SimpleApp.greeting shouldEqual "simple-application-hello"
  }
}' > src/test/scala/${PKG_DIR}/HelloSpec.scala

sbt test run