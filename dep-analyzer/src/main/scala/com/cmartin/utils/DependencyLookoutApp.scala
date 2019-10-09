package com.cmartin.utils

import com.cmartin.learn.common.{ComponentLogging, Utils}
import zio.{App, Task, UIO}

import scala.util.matching.Regex

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyLookoutApp
  extends App
    with ComponentLogging {

  import Domain._

  val pattern = raw"(^[a-z][a-z0-9-_\.]+):([a-z0-9-_\.]+):([0-9A-Za-z-\.]+)".r


  /*
     E X E C U T I O N
   */

  val httpManager = HttpManager()

  val program = for {
    lines <- FileManager.getLinesFromFile("dep-analyzer/src/main/resources/deps.log")
    dependencies <- FileManager.parseLines(lines)
    validDeps <- FileManager.filterValid(dependencies)
    validRate <- Task.succeed(100.toDouble * validDeps.size / dependencies.size)
    finalDeps <- Task.succeed(validDeps.filterNot(_.group.startsWith("com.cmartin")))
    remoteDeps <- httpManager.checkDependencies2(finalDeps)
  } yield (finalDeps, validRate)

  //httpManager.checkDependencies(exec)


  //https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
  //val uri = raw"https://search.maven.org/solrsearch/select?q=g:com.typesafe.akka%20AND%20a:akka-actor_2.13%20AND%20v:2.5.25%20AND%20p:jar&rows=1&wt=json"
  override def run(args: List[String]): UIO[Int] = {

    val (deps,rate) = unsafeRun(program)

    //log.info(s"Valid rate of dependencies in the file: $validRate")

    //log.info(s"Dependency list[${deps.size}]")

    log.debug(Utils.prettyPrint(deps))
    log.debug((s"valid rate: $rate %"))

    UIO(0)
  }
}

/* old code
  val parsedDeps: Seq[Either[String, Dep]] =
    parseDependencies("dep-analyzer/src/main/resources/deps.log")

  val validDeps: Seq[Dep] =
    parsedDeps
      .collect {
        case Right(dep) => dep
      }

  val validRate: Double = 100.toDouble * validDeps.size / parsedDeps.size

    log.debug(s"valid rate: $validRate")

 */
