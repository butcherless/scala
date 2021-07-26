#!/bin/bash

PROJECT_NAME="project-template"
PKG_DIR=com/cmartin/learn
SOURCE_PKG=com.cmartin.learn
SCALA_VER="2.13.6"
SBT_VER="1.5.5"
SBT_ASSEMBLY_VER="1.0.0"
SBT_BLOOP_VER="1.4.8-81-e170cd66"
SBT_SCALAFMT_VER="2.4.3"
DEP_GRAPH_VER="0.10.0-RC1"
DEP_UP_VER="1.2.2"
LOGBACK_VER="1.2.5"
SCALAFMT_VER="2.7.5"
SCALATEST_VER="3.2.9"
SCOVERAGE_VER="1.8.2"
SLF4ZIO_VER="1.0.0"
ZIO_VER="1.0.9"

#
# create filesystem
#
mkdir -p project src/{main,test}/{resources,scala/${PKG_DIR}}


#
# create project properties file
#
echo "sbt.version=${SBT_VER}" > project/build.properties


#
# create code format file
#
echo 'version = "'${SBT_SCALAFMT_VER}'"
align = more // For pretty alignment.
maxColumn = 120
docstrings = ScalaDoc
' > .scalafmt.conf

#
# create sbt plugins file
#
echo '// sbt tool plugins
addSbtPlugin("com.eed3si9n"       % "sbt-assembly"           % "'${SBT_ASSEMBLY_VER}'")
addSbtPlugin("net.virtual-void"   % "sbt-dependency-graph"   % "'${DEP_GRAPH_VER}'")
addSbtPlugin("org.jmotor.sbt"     % "sbt-dependency-updates" % "'${DEP_UP_VER}'")
addSbtPlugin("org.scoverage"      % "sbt-scoverage"          % "'${SCOVERAGE_VER}'")
addSbtPlugin("org.scalameta"      % "sbt-scalafmt"           % "'${SBT_SCALAFMT_VER}'")
addSbtPlugin("ch.epfl.scala"      % "sbt-bloop"              % "'${SBT_BLOOP_VER}'")
' > project/plugins.sbt



#
# create project version file
#
echo 'ThisBuild / version := "1.0.0-SNAPSHOT"
' > version.sbt


#
# create versions file
#
echo 'object Versions {

  val logback = "'${LOGBACK_VER}'"
  val slf4zio = "'${SLF4ZIO_VER}'"
  val zio     = "'${ZIO_VER}'"

  val scalatest = "'${SCALATEST_VER}'"

}' > project/Versions.scala


#
# create dependencies file
#
echo 'import sbt._

object Dependencies {

  val mainAndTest = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "com.github.mlangc" %% "slf4zio" % Versions.slf4zio,
    "dev.zio" %% "zio" % Versions.zio,
    
    // TESTING
    
    "org.scalatest" %% "scalatest" % Versions.scalatest % Test,
    "dev.zio" %% "zio-test" % Versions.zio % "test",
    "dev.zio" %% "zio-test-sbt" % Versions.zio % "test"
  )
}' > project/Dependencies.scala


#
# create sbt build file
#
echo 'import Dependencies._

ThisBuild / scalaVersion := "'${SCALA_VER}'"
ThisBuild / organization := "com.cmartin.learn"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val basicScalacOptions = Seq(       // some of the Rob Norris tpolecat options
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-explaintypes",                     // Explain type errors in more detail.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
    "-language:higherKinds",             // Allow higher-kinded types
    "-language:implicitConversions",     // Allow definition of implicit functions called views
    "-language:postfixOps",
    "-Xlint:unused"
  )

lazy val commonSettings = Seq(
    libraryDependencies ++= mainAndTest,
    scalacOptions ++= basicScalacOptions
)

lazy val templateProject = (project in file("."))
  .settings(
      commonSettings,
      name := "'${PROJECT_NAME}'",
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )' > build.sbt


#
# create logback XML config file
#
echo '<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{ISO8601} %-5level [%thread] %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>target/template-application.log</file>
        <append>false</append>
        <encoder>
            <pattern>%date{ISO8601} %-5level [%thread] %logger{40}:%line - %msg%n%rEx</pattern>
        </encoder>
    </appender>

    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="STDOUT"/>
    </appender>


    <root level="DEBUG">
        <appender-ref ref="ASYNC"/>
    </root>
</configuration>' > src/main/resources/logback.xml


#
# create common library object
#
echo 'package '${SOURCE_PKG}'

import zio.Task

object Library {

  val TEXT = "simple-application-hello"

  def echo(message: String): String = {
    message
  }

  def sum(a: Int, b: Int): Task[Int] = {
    Task.effectTotal(a + b)
  }

}' > src/main/scala/${PKG_DIR}/Library.scala


#
# create main application
#
echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import com.github.mlangc.slf4zio.api._
import zio.{App, ExitCode, ZIO}

object SimpleApp
  extends App
    with LoggingSupport {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val program = for {
      _ <- logger.debugIO(echo(TEXT))
      result <- sum(2, 3)
      _ <- logger.debugIO(s"sum result: $result")
    } yield ()

    program.fold(e => {
      logger.errorIO("program error:", e)
      ExitCode.failure
    },
      _ => ExitCode.success)
  }
}' > src/main/scala/${PKG_DIR}/SimpleApp.scala


#
# scalatest template
#
echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LibrarySpec
  extends AnyFlatSpec
    with Matchers {

  behavior of "LibrarySpec"

  it should "return the same text" in {
    val result = echo(TEXT)

    result shouldBe TEXT
  }
}' > src/test/scala/${PKG_DIR}/LibrarySpec.scala

#
# ziotest template
#
echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import zio.test.Assertion._
import zio.test._

object ZioSpec
  extends DefaultRunnableSpec {

  def spec = suite("my spec")(
    test("my test")
    (assert(1 + 1)
    (
      equalTo(2))
    ),

    test("Echo function return the same text")
    (assert(echo(TEXT))
    (
      equalTo(TEXT))
    ),

    testM("Zio effect sum 2 + 3")
    (
      for {
        r <- sum(2, 3)
      } yield assert(r)(equalTo(5))
    )

  )
}
' > src/test/scala/${PKG_DIR}/ZioSpec.scala


#
# run this script
#
sbt clean coverage test coverageReport dependencyUpdates assembly sbtVersion run
