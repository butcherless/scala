#!/bin/bash

PROJECT_NAME="project-template"
SOURCE_PKG=com.cmartin.learn
PKG_DIR=`echo ${SOURCE_PKG} | sed 's/\./\//g'`
SCALA_VER="2.13.10"
SBT_VER="1.8.2"
SBT_ASSEMBLY_VER="2.1.1"
SBT_BLOOP_VER="1.5.6"
SBT_SCALAFMT_VER="2.4.6"
DEP_UP_VER="1.2.7"
SCALAFMT_VER="3.7.3"
SCALATEST_VER="3.2.15"
SCOVERAGE_VER="2.0.7"
ZIO_VER="2.0.13"

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
echo 'version = "'${SCALAFMT_VER}'"
align.preset = most
maxColumn = 120
newlines.source = keep
lineEndings = preserve
runner.dialect = scala213source3
docstrings = JavaDoc
docstrings.wrapMaxColumn = 80
project.git = true' > .scalafmt.conf

#
# create sbt plugins file
#
echo '
addSbtPlugin("com.eed3si9n"     % "sbt-assembly"           % "'${SBT_ASSEMBLY_VER}'")
addSbtPlugin("org.scoverage"    % "sbt-scoverage"          % "'${SCOVERAGE_VER}'")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"           % "'${SBT_SCALAFMT_VER}'")
addSbtPlugin("ch.epfl.scala"    % "sbt-bloop"              % "'${SBT_BLOOP_VER}'")
addSbtPlugin("org.jmotor.sbt"   % "sbt-dependency-updates" % "'${DEP_UP_VER}'")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph"   % "0.10.0-RC1")
addDependencyTreePlugin
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
  // main
  val zio       = "'${ZIO_VER}'"

  // test
  val scalatest = "'${SCALATEST_VER}'"

}' > project/Versions.scala


#
# create dependencies file
#
echo 'import sbt._

object Dependencies {

  val mainAndTest = Seq(
    // MAIN
    "dev.zio" %% "zio" % Versions.zio,
    
    // TEST
    "org.scalatest" %% "scalatest" % Versions.scalatest % Test
  )
}' > project/Dependencies.scala


#
# create sbt build file
#
echo 'import Dependencies._

ThisBuild / scalaVersion := "'${SCALA_VER}'"
ThisBuild / organization := "'${SOURCE_PKG}'"

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
      name := "'${PROJECT_NAME}'"
  )

 // clear screen and banner
 lazy val cls = taskKey[Unit]("Prints a separator")
 cls := {
   val brs = "\n".repeat(2)
   val message = "* B U I L D   B E G I N S   H E R E *"
   val chars = "*".repeat(message.length())
   println(s"$brs$chars")
   println("* B U I L D   B E G I N S   H E R E *")
   println(s"$chars$brs ")
 }

// command aliases
addCommandAlias("xdep-list", "dependencyList/toFile /tmp/dep-list.log -f")
addCommandAlias("xcoverage", "clean;coverage;test;coverageReport")
addCommandAlias("xdep-up", "dependencyUpdates")
' > build.sbt


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

    <!-- application loggers -->
    <logger name="'${SOURCE_PKG}'" level="DEBUG"/>

    <root level="INFO">
        <appender-ref ref="ASYNC"/>
    </root>
</configuration>' > src/main/resources/logback.xml


#
# create common library object
#
echo 'package '${SOURCE_PKG}'

import zio._

object Library {

  val TEXT = "simple-application-hello"

  def echo(message: String): String = {
    message
  }

  def sum(a: Int, b: Int): UIO[Int] = {
    ZIO.succeed(a + b)
  }

}' > src/main/scala/${PKG_DIR}/Library.scala


#
# create main application
#
echo 'package '${SOURCE_PKG}'

import com.cmartin.learn.Library._
import zio._

object SimpleApp
    extends ZIOAppDefault {

  val logAspect = ZIOAspect.loggedWith[Int](r => s"sum result: $r")

  def run = {
    for {
      _      <- ZIO.log(echo(TEXT))
      result <- sum(2, 3) @@ logAspect
    } yield ()
  }
}' > src/main/scala/${PKG_DIR}/SimpleApp.scala


#
# scalatest template
#
echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.{default => runtime}
import zio.Unsafe

class LibrarySpec
    extends AnyFlatSpec
    with Matchers {

  behavior of "Library"

  it should "return the same text" in {
    val result = echo(TEXT)

    result shouldBe TEXT
  }

  it should "sum two numbers" in {
    // given
    val a = 1
    val b = 2

    // when
    val program = sum(a, b)
    val result  = Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(program).getOrThrowFiberFailure()
    }

    // then
    result shouldBe a + b
  }
  
}' > src/test/scala/${PKG_DIR}/LibrarySpec.scala


#
# run this script
#
sbt clean update scalaVersion sbtVersion coverage test coverageReport dependencyUpdates assembly run
