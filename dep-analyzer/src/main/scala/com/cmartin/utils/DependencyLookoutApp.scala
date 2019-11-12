package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain.{ComparationResult, Same}
import com.cmartin.utils.environment._
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

  val filename = "dep-analyzer/src/main/resources/deps2.log"
  val exclusionList = List("com.globalavl.core", "com.globalavl.hiber.services")

  //val depsManaged: ZManaged[HttpManager, Nothing, Unit] = ZManaged.make(getEnvironment())(_ => shutdown())

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

  type Environments = FileManager with LogicManager with HttpManager //TODO  with ConfigManager

  trait AppModules extends FileManagerLive with LogicManagerLive with HttpManagerLive

  val modules = new AppModules {
    override implicit val backend: SttpBackend[Task, Nothing] = AsyncHttpClientZioBackend()
  }

  val program: ZIO[Environments, Throwable, Unit] = for {
    lines <- getLinesFromFile(filename)
    dependencies <- parseLines(lines)
    _ <- logDepCollection(dependencies)
    validDeps <- filterValid(dependencies)
    validRate <- calculateValidRate(dependencies.size, validDeps.size)
    finalDeps <- excludeList(validDeps, exclusionList)
    remoteDeps <- getEnvironment().bracket(_ => shutdown())(_ => checkDependencies(finalDeps))
    _ <- logMessage(s"Valid rate of dependencies in the file: $validRate %")
    _ <- logPairCollection(remoteDeps)
  } yield ()

  /*
     E X E C U T I O N
   */
  override def run(args: List[String]): UIO[Int] = {
    unsafeRun(program.provide(modules).either)
      .fold(_ => UIO(1), _ => UIO(0))
  }

  //TODO identificar patrones de dependencias
  // comprobar si ambas pertenecen al mismo patrón
  // TODO VersionManager
  def compareVersions(local: String, remote: String): ComparationResult = {
    if (local == remote) Same
    else ???
  }
}
