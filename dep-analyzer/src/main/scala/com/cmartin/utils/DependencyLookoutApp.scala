package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import zio.{App, UIO}

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyLookoutApp
  extends App
    with ComponentLogging {

  import environment._

  /*
     E X E C U T I O N
   */

  val httpManager = HttpManager()

  val program = for {
    lines <- getLinesFromFile("dep-analyzer/src/main/resources/deps2.log")
    dependencies <- parseLines(lines)
    _ <- logDepCollection(dependencies)
    validDeps <- filterValid(dependencies)
    validRate <- UIO.succeed(100.toDouble * validDeps.size / dependencies.size)
    finalDeps <- UIO.succeed(validDeps.filterNot(_.group.startsWith("com.cmartin")))
    //remoteDeps <- httpManager.checkDependencies(finalDeps)
    //TODO shutdown backend httpManager.shutdown
  } yield (finalDeps, validRate)

  override def run(args: List[String]): UIO[Int] = {

    val (deps, rate) = unsafeRun(program.provide(FileManagerLive))

    log.info(s"Valid rate of dependencies in the file: $rate")

    log.debug((s"valid rate: $rate %"))

    //    deps.foreach {
    //      case Left(error) => log.info(s"$RESET$RED${error.toString}$RESET")
    //      case Right(tuple) => if (tuple._1 != tuple._2) log.info(FileManager.formatChanges(tuple))
    //    }

    UIO(0) //TODO exit code
  }

  def logCollection(collection: Seq[_]) =
    collection.foreach(elem => log.debug(elem.toString))

  def logEitherCollection(collection: List[Either[_,_]]) = ???

}
