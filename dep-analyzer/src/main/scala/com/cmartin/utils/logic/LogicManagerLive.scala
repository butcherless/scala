package com.cmartin.utils.logic

import com.cmartin.utils.model.Domain
import com.cmartin.utils.model.Domain.Gav
import zio._

import scala.util.matching.Regex

case class LogicManagerLive()
    extends LogicManager {

  type ParseError = String

  val pattern: Regex =
    raw"(^[a-z][a-z0-9-_.]+):([a-zA-Z0-9-_.]+):([0-9A-Za-z-.]+)".r

  override def parseLines(lines: List[String]): UIO[(Iterable[ParseError], Iterable[Gav])] =
    ZIO.partitionPar(lines)(parseDepLine).withParallelism(4)

  override def filterValid(dependencies: List[Either[String, Domain.Gav]]): UIO[List[Domain.Gav]] =
    UIO.succeed(dependencies.collect { case Right(dep) => dep })

  override def excludeFromList(
      dependencies: Iterable[Domain.Gav],
      exclusions: List[String]
  ): UIO[Iterable[Domain.Gav]] =
    UIO.succeed(
      dependencies.filterNot(dep => exclusions.contains(dep.group))
    )

  override def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double] =
    UIO.succeed(100.toDouble * validCount / dependencyCount)

  /*
    H E L P E R S
   */

  private def parseDepLine(line: String): IO[String, Gav] = {
    for {
      _        <- ZIO.logInfo(s"parsing line: $line")
      iterator <- UIO.succeed(pattern.findAllMatchIn(line))
      result   <- ZIO.ifZIO(UIO.succeed(iterator.hasNext))(
                    IO.succeed(Gav.fromRegexMatch(iterator.next())),
                    IO.fail(line)
                  )
    } yield result
  }

}

object LogicManagerLive extends (() => LogicManager) {
  val layer: ULayer[LogicManager] =
    LogicManagerLive.toLayer
}
