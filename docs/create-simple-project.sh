#!/bin/bash

PKG_DIR=com/cmartin/learn
SOURCE_PKG=com.cmartin.learn
SBT_VER=1.3.7
SCALA_VER="2.13.1"
ASSEMBLY_VER="0.14.10"
DEP_GRAPH_VER="0.10.0-RC1"
DEP_UP_VER="1.2.1"
LOGBACK_VER="1.2.3"
SCALATEST_VER="3.1.0"
SCOVERAGE_VER="1.6.1"
ZIO_VER="1.0.0-RC17"

# create filesystem
mkdir -p project src/{main,test}/{resources,scala} src/main/scala/${PKG_DIR} src/test/scala/${PKG_DIR}


# create project properties file
echo "sbt.version=${SBT_VER}" > project/build.properties


# create sbt plugins file
echo 'addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "'${ASSEMBLY_VER}'")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "'${DEP_GRAPH_VER}'")
addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "'${DEP_UP_VER}'")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "'${SCOVERAGE_VER}'")' > project/plugins.sbt

# create dependencies file
echo 'import sbt._

object Dependencies {
  lazy val logbackVersion = "'${LOGBACK_VER}'"
  lazy val zioVersion = "'${ZIO_VER}'"

  lazy val scalatestVersion = "'${SCALATEST_VER}'"

  val mainAndTest = Seq(
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "dev.zio" %% "zio" % zioVersion,

    "org.scalatest" %% "scalatest" % scalatestVersion % Test,

    "dev.zio" %% "zio-test" % zioVersion % "test",
    "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
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
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )' > build.sbt

echo '-server
-Xms512M
-Xmx3G
-Xss1M
-XX:NewRatio=8' > .jvmopts


# create logback XML config file
echo '<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{ISO8601} %-5level [%thread] %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="STDOUT"/>
    </appender>


    <root level="DEBUG">
        <appender-ref ref="ASYNC"/>
    </root>
</configuration>' > src/main/resources/logback.xml


# create common library object
echo 'package '${SOURCE_PKG}'

object Library {

  import zio.UIO

  val TEXT = "simple-application-hello"

  def echo(message: String): String = {
    message
  }

  def sum(a: Int, b: Int): UIO[Int] = {
    UIO.effectTotal(a + b)
  }

}' > src/main/scala/${PKG_DIR}/Library.scala


# create main application
echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import org.slf4j.LoggerFactory
import zio.{App, Task, ZIO}

object SimpleApp extends App {

    private val log = LoggerFactory.getLogger(classOf[App])

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    val program = for {
      _ <- Task.effect(log.debug(echo(TEXT)))
      result <- sum(2, 3)
      _ <- Task.effect(log.debug(s"sum result: $result"))
    } yield ()

    program.fold(e => {
      println(e)
      1
    },
      _ => 0)
  }
}' > src/main/scala/${PKG_DIR}/SimpleApp.scala


echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LibrarySpec extends AnyFlatSpec with Matchers {
  "LibrarySpec echo" should "return the same text" in {
    val result = echo(TEXT)

    result shouldBe TEXT
  }
}' > src/test/scala/${PKG_DIR}/LibrarySpec.scala

echo 'package '${SOURCE_PKG}'

import '${SOURCE_PKG}'.Library._
import zio.test.Assertion._
import zio.test._

object ZioSpec
  extends DefaultRunnableSpec(
    suite("Check test")(
      test("Echo function return the same text") {
        val result = echo(TEXT)
        assert(result, equalTo(TEXT))
      },
      testM("Zio effect sum 2 + 3") {
        for {
          r <- sum(2, 3)
        } yield assert(r, equalTo(5))
      }
    )
  )
' > src/test/scala/${PKG_DIR}/ZioSpec.scala



sbt clean coverage test coverageReport dependencyUpdates assembly sbtVersion run
