package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.config.ConfigHelper.AppConfig
import com.cmartin.utils.file._
import com.cmartin.utils.http._
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import zio.config.{ReadError, _}
import zio.{Clock, _}

import java.util.concurrent.TimeUnit

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
  https://twitter.com/jdegoes/status/1462758239418867714  ZLayer
  https://twitter.com/jdegoes/status/1463261876150849547
 */

object DependencyLookoutApp
    extends ZIOAppDefault
    with ComponentLogging {

  // TODO add zio config to read application properties
  // val filename      = "/tmp/dep-list.log"       // TODO property
  // val exclusionList = List("com.cmartin.learn") // TODO property
  val configMap: Map[String, String] = Map(
    "FILENAME"   -> "/tmp/dep-list.log",
    "EXCLUSIONS" -> "com.cmartin.learn"
  )

  /*
    ZIO[R, E, A]
      - R: application module/s
      - E: error in case of failure
      - A: returned value

    Examples:
      - R = { ConfigManger, FileManager, LogicManager, HttpManager }
      - E: ConnectionError (ADT)
      - A: Either[Exception, Results]
   */

  val program = for {
    config                   <- getConfig[AppConfig]
    startTime                <- zio.Clock.currentTime(TimeUnit.MILLISECONDS)
    lines                    <- FileManager(_.getLinesFromFile(config.filename))
    (parseErrors, validDeps) <- LogicManager(_.parseLines(lines))
    validRate                <- LogicManager(_.calculateValidRate(lines.size, validDeps.size))
    _                        <- ZIO.logInfo(s"Valid rate of dependencies in the file: $validRate %")
    finalDeps                <- LogicManager(_.excludeFromList(validDeps, List(config.exclusions)))
    (errors, remoteDeps)     <- HttpManager(_.checkDependencies(finalDeps))
    // TODO process errors
    _                        <- FileManager(_.logPairCollection(remoteDeps))
    _                        <- FileManager(_.logWrongDependencies(errors))
    stopTime                 <- zio.Clock.currentTime(TimeUnit.MILLISECONDS)
    _                        <- ZIO.log(s"processing time: ${stopTime - startTime} milliseconds")

  } yield ()

  val configLayer: Layer[ReadError[String], AppConfig] =
    ZConfig.fromMap(configMap, ConfigHelper.configDescriptor)

  val programLayer = {
    Clock.live ++
      configLayer ++
      FileManagerLive.layer ++
      LogicManagerLive.layer ++
      HttpManagerLive.layer
  }

  /*
     E X E C U T I O N
   */
  override def run = {
    /*
     This is similar to dependency injection, and the `provide` function can be
     thought of as `inject`.
     */

    program
      .provide(programLayer)
  }
}
