package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils.{colourBlue, colourGreen, colourRed}
import zio.{App, UIO}

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyLookoutApp extends App with ComponentLogging {

  import environment._

  /*
     E X E C U T I O N
   */

  val httpManager = HttpManager()

  val program = for {
    lines        <- getLinesFromFile("dep-analyzer/src/main/resources/deps2.log")
    dependencies <- parseLines(lines)
    _            <- logDepCollection(dependencies)
    validDeps    <- filterValid(dependencies)
    validRate    <- UIO.succeed(100.toDouble * validDeps.size / dependencies.size)
    finalDeps    <- UIO.succeed(validDeps.filterNot(_.group.startsWith("com.cmartin")))
    remoteDeps   <- httpManager.checkDependencies(finalDeps)
    //TODO shutdown backend httpManager.shutdown
  } yield (remoteDeps, validRate)

  override def run(args: List[String]): UIO[Int] = {

    val (deps, rate) = unsafeRun(program.provide(FileManagerLive))

    log.info(s"Valid rate of dependencies in the file: $rate")

    log.debug((s"valid rate: $rate %"))

    deps.foreach {
      case Left(error) => log.info(s"${colourRed(error.toString)}")
      case Right(pair) => if (pair.hasNewVersion()) log.info(formatChanges(pair))
    }

    UIO(0) //TODO exit code
  }

  def formatChanges(pair: Domain.GavPair): String =
    s"${pair.local.formatShort} ${colourGreen("=>")} ${colourBlue(pair.remote.version)}"

  def logCollection(collection: Seq[_]): Unit =
    collection.foreach(elem => log.debug(elem.toString))

  def logEitherCollection(collection: List[Either[_, _]]) = ???

}
