package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.file.FileManager
import com.cmartin.utils.http.HttpManager
import com.cmartin.utils.logic.LogicManager
import zio.{App, UIO, ZIO}

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyLookoutApp extends App with ComponentLogging {

  import ApplicationHelper._

  val filename = "dep-analyzer/src/main/resources/deps3.log" // TODO property
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


  val program: ZIO[Environments, Throwable, Unit] = for {
    lines <- FileManager.>.getLinesFromFile(filename)
    dependencies <- LogicManager.>.parseLines(lines)
    _ <- FileManager.>.logDepCollection(dependencies)
    validDeps <- LogicManager.>.filterValid(dependencies)
    validRate <- LogicManager.>.calculateValidRate(dependencies.size, validDeps.size)
    finalDeps <- LogicManager.>.excludeList(validDeps, exclusionList)
    remoteDeps <- HttpManager.managed().use(_.httpManager.checkDependencies(finalDeps))
    _ <- FileManager.>.logMessage(s"Valid rate of dependencies in the file: $validRate %")
    _ <- FileManager.>.logPairCollection(remoteDeps)
  } yield ()

  /*
     E X E C U T I O N
   */
  override def run(args: List[String]): UIO[Int] = {
    unsafeRun(program.provide(modules).either)
      .fold(_ => UIO(1), _ => UIO(0))
  }
}
