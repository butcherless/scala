package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain.{ComparationResult, Same}
import com.cmartin.utils.environment.{FileManager, FileManagerLive, HttpManager, HttpManagerLive, LogicManager, LogicManagerLive}
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.{App, Task, UIO, ZIO}

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyLookoutApp extends App with ComponentLogging {
  import environment.FileManagerHelper._
  import environment.HttpManagerHelper._
  import environment.LogicManagerHelper._

  val filename      = "dep-analyzer/src/main/resources/deps2.log"
  val exclusionList = List("com.globalavl.core", "com.globalavl.hiber.services")

  //val depsManaged: ZManaged[HttpManager, Nothing, Unit] = ZManaged.make(getEnvironment())(_ => shutdown())

  /*
      ZIO[R, E, A]
      - R: modules sequence
      - E: Error in case of failure
      - A: Value returned

      R = ConfigManger :: FileManager :: LogicManager :: HttpManager :: ...
   */

//  object AppModules extends FileManagerLive with LogicManagerLive with HttpManagerLive {
//    implicit val backend: SttpBackend[Task, Nothing] = AsyncHttpClientZioBackend()
//  }

  trait Environments extends FileManager with LogicManager with HttpManager
  trait AppModules   extends FileManagerLive with LogicManagerLive with HttpManagerLive
  val modules = new AppModules {
    override implicit val backend: SttpBackend[Task, Nothing] = AsyncHttpClientZioBackend()
  }

  val program: ZIO[FileManager with LogicManager with HttpManager, Throwable, Unit] = for {
    lines        <- getLinesFromFile(filename)
    dependencies <- parseLines(lines)
    _            <- logDepCollection(dependencies)
    validDeps    <- filterValid(dependencies)
    validRate    <- calculateValidRate(dependencies.size, validDeps.size)
    finalDeps    <- excludeList(validDeps, exclusionList)
    remoteDeps   <- getEnvironment().bracket(_ => shutdown())(_ => checkDependencies(finalDeps))
    _            <- logMessage(s"Valid rate of dependencies in the file: $validRate %")
    _            <- logPairCollection(remoteDeps)
  } yield ()

  /*
     E X E C U T I O N
   */
  override def run(args: List[String]): UIO[Int] = {
    unsafeRun(program.provide(modules).either)
      .fold(_ => UIO(1), _ => UIO(0))
  }

  //TODO move to common object

  //TODO identificar patrones de dependencias
  // comprobar si ambas pertenecen al mismo patrón
  // TODO VersionManager
  def compareVersions(local: String, remote: String): ComparationResult = {
    if (local == remote) Same
    else ???
  }
}
