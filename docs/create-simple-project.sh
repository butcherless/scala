#!/bin/bash

PKG_DIR=com/cmartin/learn
SOURCE_PKG=com.cmartin.learn
SBT_VER=1.2.8
SCALA_VER="2.13.0"
ASSEMBLY_VER="0.14.10"
LOGBACK_VER="1.2.3"
SCALATEST_VER="3.0.8"
SCOVERAGE_VER="1.6.0"
DEP_UP_VER="1.2.1"

# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}


# create project properties file
echo "sbt.version=${SBT_VER}" > project/build.properties


# create sbt plugins file
echo 'addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "'${ASSEMBLY_VER}'")
addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "'${DEP_UP_VER}'")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "'${SCOVERAGE_VER}'")' > project/plugins.sbt


# create dependencies file
echo 'import sbt._

object Dependencies {
  lazy val scalatestVersion = "'${SCALATEST_VER}'"
  lazy val logbackVersion = "'${LOGBACK_VER}'"

  val mainAndTest = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,

    "org.scalatest" %% "scalatest" % scalatestVersion % Test
  )
}' > project/Dependencies.scala


# create sbt build file
echo 'import Dependencies._

lazy val basicScalacOptions = Seq(       // some of the Rob Norris tpolecat options
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

lazy val commonSettings = Seq(
    organization := "com.cmartin.learn",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "'${SCALA_VER}'",
    libraryDependencies ++= mainAndTest,
    scalacOptions ++= basicScalacOptions,
    test in assembly := {}
)

lazy val templateProject = (project in file("."))
  .settings(
      commonSettings,
      name := "project-template",
  )' > build.sbt


# create logback XML config file
echo '<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
        <encoder>
            <pattern>%date %-5level [%10thread] %-40logger:%line{15} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="DEBUG">
        <appender-ref ref="stdout"/>
    </root>
</configuration>' > src/main/resources/logback.xml


# create common library object
echo 'package '${SOURCE_PKG}'

object Library {
  val TEXT = "simple-application-hello"

  def echo(message: String): String = {
    message
  }
}' > src/main/scala/${PKG_DIR}/Library.scala


# create main application
echo 'package '${SOURCE_PKG}'

import com.cmartin.learn.Library._
import org.slf4j.LoggerFactory

object SimpleApp extends App {

    private val log = LoggerFactory.getLogger(classOf[App])

    log.debug(echo(TEXT))
}' > src/main/scala/${PKG_DIR}/SimpleApp.scala


echo 'package '${SOURCE_PKG}'

import com.cmartin.learn.Library._
import org.scalatest._

class LibrarySpec extends FlatSpec with Matchers {
  "LibrarySpec echo" should "return the same text" in {
    val result = echo(TEXT)

    result shouldBe TEXT
  }
}' > src/test/scala/${PKG_DIR}/LibrarySpec.scala

sbt -batch clean coverage test coverageReport dependencyUpdates assembly run
