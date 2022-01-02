package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.file._
import com.cmartin.utils.http._
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import zio._

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
  https://twitter.com/jdegoes/status/1462758239418867714  ZLayer
  https://twitter.com/jdegoes/status/1463261876150849547
 */

object DependencyLookoutApp
    extends ZIOAppDefault
    with ComponentLogging {

  val filename = "/tmp/dep-list.log" // TODO property
  val exclusionList = List("com.globalavl.core", "com.globalavl.hiber.services") // TODO property

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
    lines <- FileManager(_.getLinesFromFile(filename))
    (dependencies, validDeps) <- LogicManager(_.parseLines(lines))
    // _ <- FileManager(_.logDepCollection(dependencies))
    validRate <- LogicManager(_.calculateValidRate(lines.size, validDeps.size))
    _ <- ZIO.logInfo(s"Valid rate of dependencies in the file: $validRate %")
    finalDeps <- LogicManager(_.excludeList(validDeps, exclusionList))
    (errors, remoteDeps) <- HttpManager(_.checkDependencies(finalDeps))
    // TODO process errors
    _ <- FileManager(_.logPairCollection(remoteDeps))
    _ <- FileManager(_.logWrongDependencies(errors))
  } yield ()

  type Services = FileManager with LogicManager with HttpManager

  val programLayer =
    FileManagerLive.layer ++
      LogicManagerLive.layer ++
      HttpManagerLive.layer

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
