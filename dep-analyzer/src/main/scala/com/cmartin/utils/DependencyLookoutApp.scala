package com.cmartin.utils

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.file._
import com.cmartin.utils.http._
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import zio._
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

import java.util.concurrent.TimeUnit

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
  https://twitter.com/jdegoes/status/1462758239418867714  ZLayer
  https://twitter.com/jdegoes/status/1463261876150849547
 */

object DependencyLookoutApp
    extends ZIOAppDefault {

  /*
    ZIO[R, E, A]
      - R: application services / dependencies
      - E: error in case of failure
      - A: returned value

    Examples:
      - R = { ConfigManger, FileManager, LogicManager, HttpManager }
      - E: ConnectionError (part of an ADT)
      - A: Either[Exception, Results]
   */

  type ApplicationDependencies =
    Clock with FileManager with HttpManager with LogicManager

  val logAspect: RuntimeConfigAspect =
    SLF4J.slf4j(
      logLevel = LogLevel.Debug,
      format = LogFormat.line
    )

  override def hook: RuntimeConfigAspect = logAspect

  /* E X E C U T I O N
     This is similar to dependency injection and
     the `provide` function can be thought of as `inject`.
   */
  override def run = {

    val applicationLayer: ULayer[ApplicationDependencies] =
      ZLayer.make[ApplicationDependencies](
        Clock.live,
        FileManagerLive.layer,
        HttpManagerLive.layer,
        LogicManagerLive.layer,
        ZLayer.Debug.mermaid
      )

    // TODO resolve error channel type, actual Object
    def logicProgram(filename: String) = (for {
      config                   <- ConfigHelper.readFromFile(filename)
      startTime                <- Clock.currentTime(TimeUnit.MILLISECONDS)
      lines                    <- FileManager(_.getLinesFromFile(config.filename))
      (parseErrors, validDeps) <- LogicManager(_.parseLines(lines))
      _                        <- ZIO.logInfo(s"parsingErrors: $parseErrors")
      validRate                <- LogicManager(_.calculateValidRate(lines.size, validDeps.size))
      _                        <- ZIO.logInfo(s"Valid rate of dependencies in the file: $validRate %")
      finalDeps                <- LogicManager(_.excludeFromList(validDeps, config.exclusions))
      (errors, remoteDeps)     <- HttpManager(_.checkDependencies(finalDeps))
      // TODO process errors
      _                        <- FileManager(_.logPairCollection(remoteDeps))
      _                        <- FileManager(_.logWrongDependencies(errors))
      stopTime                 <- Clock.currentTime(TimeUnit.MILLISECONDS)
      _                        <- ZIO.logInfo(s"processing time: ${stopTime - startTime} milliseconds")
    } yield ()).provide(applicationLayer)

    // main program
    for {
      args <- getArgs
      _    <- ZIO.when(args.isEmpty) {
                Console.printLine(s"Please, supply hocon config file, for example: /tmp/application-config.hocon") *>
                  IO.fail("empty command line arguments")
              }
      _    <- logicProgram(args.head)
    } yield ()
  }

}
