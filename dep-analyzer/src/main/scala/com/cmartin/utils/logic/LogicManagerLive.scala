package com.cmartin.utils.logic

import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.Gav
import zio._

import scala.util.matching.Regex

case class LogicManagerLive()
    extends LogicManager {

  val pattern: Regex =
    raw"(^[a-z][a-z0-9-_.]+):([a-zA-Z0-9-_.]+):([0-9A-Za-z-.]+)".r

  // TODO find ZIO native function to accumulate failure and success results
  type ParseError = String

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

  override def parseLines(lines: List[String]): UIO[(Iterable[ParseError], Iterable[Gav])] =
    ZIO.partitionPar(lines)(parseDepLine).withParallelism(4)

  override def filterValid(dependencies: List[Either[String, Domain.Gav]]): UIO[List[Domain.Gav]] =
    UIO.succeed(dependencies.collect { case Right(dep) => dep })

  override def excludeList(
      dependencies: Iterable[Domain.Gav],
      exclusionList: List[String]
  ): ZIO[Any, Nothing, Iterable[Domain.Gav]] =
    UIO.succeed(
      dependencies.filterNot(dep => exclusionList.contains(dep.group))
    )

  override def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double] =
    UIO.succeed(100.toDouble * validCount / dependencyCount)

  /*
    H E L P E R S
   */
}

object LogicManagerLive {
  val layer: ZLayer[Any, Nothing, LogicManager] =
    ZLayer.fromZIO(UIO.succeed(LogicManagerLive()))
}
