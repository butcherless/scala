package com.cmartin.utils.logic

import com.cmartin.utils.domain.{LogicManager, Model}
import com.cmartin.utils.domain.LogicManager.ParsedLines
import Model.Gav
import zio._

import scala.util.matching.Regex

final case class DependencyLogicManager()
    extends LogicManager {

  val pattern: Regex =
    raw"(^[a-z][a-z0-9-_.]+):([a-zA-Z0-9-_.]+):([0-9A-Za-z-.]+)".r

  override def parseLines(lines: List[String]): UIO[ParsedLines] =
    ZIO.partitionPar(lines)(parseDepLine)
      .withParallelism(4)
      .map(ParsedLines.tupled) // result tuple => constructor function

  override def filterValid(dependencies: List[Either[String, Model.Gav]]): UIO[List[Model.Gav]] =
    ZIO.succeed(dependencies.collect { case Right(dep) => dep })

  override def excludeFromList(
      dependencies: Iterable[Model.Gav],
      exclusions: List[String]
  ): UIO[Iterable[Model.Gav]] =
    ZIO.succeed(
      dependencies.filterNot(dep => exclusions.contains(dep.group))
    )

  override def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double] =
    ZIO.succeed(100.toDouble * validCount / dependencyCount)

  /*
    H E L P E R S
   */

  private def parseDepLine(line: String): IO[String, Gav] = {
    for {
      _        <- ZIO.logDebug(s"parsing line: $line")
      iterator <- ZIO.succeed(pattern.findAllMatchIn(line))
      result   <- ZIO.ifZIO(ZIO.succeed(iterator.hasNext))(
                    ZIO.succeed(Gav.fromRegexMatch(iterator.next())),
                    ZIO.fail(line)
                  )
    } yield result
  }

}

object DependencyLogicManager {
  val layer: ULayer[LogicManager] =
    ZLayer.succeed(DependencyLogicManager())
}
