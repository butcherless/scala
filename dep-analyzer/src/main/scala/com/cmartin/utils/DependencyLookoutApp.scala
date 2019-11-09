package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils.{colourBlue, colourGreen, colourRed}
import com.cmartin.utils.Domain.{ComparationResult, GavPair, RepoResult, Results, Same}
import com.cmartin.utils.environment.{FileManager, FileManagerLive}
import zio.{App, UIO, ZIO}

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyLookoutApp extends App with ComponentLogging {
  import environment.FileManagerHelper._

  val httpManager = HttpManager()

  val exclusionList = List("com.globalavl.core", "com.globalavl.hiber.services")

  /*
      ZIO[R, E, A]
      R = MyApp : ConfigManger : LogManager : FileManager : HttpManager :  Helper : ...
   */

  val program: ZIO[FileManager, Throwable, Results] = for {
    lines        <- getLinesFromFile("dep-analyzer/src/main/resources/deps2.log")
    dependencies <- parseLines(lines)
    _            <- logDepCollection(dependencies)
    validDeps    <- filterValid(dependencies)
    validRate    <- UIO.succeed(100.toDouble * validDeps.size / dependencies.size)
    finalDeps    <- excludeList(validDeps, exclusionList)
    remoteDeps   <- httpManager.checkDependencies(finalDeps)
    _            <- httpManager.shutdown()
  } yield Results(remoteDeps, validRate)

  /*
     E X E C U T I O N
   */
  override def run(args: List[String]): UIO[Int] = {
    val results: Results = unsafeRun(program.provide(FileManagerLive))

    log.info(s"Valid rate of dependencies in the file: ${results.validRate} %")

    logEitherCollection(results.pairs)

    UIO(0) //TODO exit code
  }

  //TODO move to common object

  //TODO identificar patrones de dependencias
  // comprobar si ambas pertenecen al mismo patrón
  def compareVersions(local: String, remote: String): ComparationResult = {
    if (local == remote) Same
    else ???
  }

  def formatChanges(pair: Domain.GavPair): String =
    s"${pair.local.formatShort} ${colourGreen("=>")} ${colourBlue(pair.remote.version)}"

  def logCollection(collection: Seq[_]): Unit =
    collection.foreach(elem => log.debug(elem.toString))

  def logEitherCollection(collection: List[RepoResult[GavPair]]): Unit =
    collection.foreach {
      case Left(error) => log.info(s"${colourRed(error.toString)}")
      case Right(pair) => if (pair.hasNewVersion()) log.info(formatChanges(pair))
    }
}
