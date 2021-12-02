package com.cmartin.utils.logic

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.Gav
import zio._

import scala.util.matching.Regex

case class LogicManagerLive()
    extends LogicManager
    with ComponentLogging {

  val pattern: Regex =
    raw"(^[a-z][a-z0-9-_\.]+):([a-zA-Z0-9-_\.]+):([0-9A-Za-z-\.]+)".r

  override def parseLines(lines: List[String]): ZIO[Any, Nothing, List[Either[String, Domain.Gav]]] =
    UIO.foreach(lines)(line => UIO.succeed(parseDepLine(line)))

  override def filterValid(dependencies: List[Either[String, Domain.Gav]]): ZIO[Any, Nothing, List[Domain.Gav]] =
    UIO.succeed(dependencies.collect { case Right(dep) => dep })

  override def excludeList(
      dependencies: List[Domain.Gav],
      exclusionList: List[String]
  ): ZIO[Any, Nothing, List[Domain.Gav]] =
    UIO.succeed(
      dependencies.filterNot(dep => exclusionList.contains(dep.group))
    )

  override def calculateValidRate(dependencyCount: Int, validCount: Int): ZIO[Any, Nothing, Double] =
    UIO.succeed(100.toDouble * validCount / dependencyCount)

  /*
    H E L P E R S
   */
  private def parseDepLine(line: String): Either[String, Gav] = {
    log.debug(s"reading dependency candidate: $line matches regex? $matches")
    lazy val matches = pattern.matches(line)

    if (matches) {
      val regexMatch: Regex.Match = pattern.findAllMatchIn(line).next()
      Right(Gav.fromRegexMatch(regexMatch))
    } else {
      Left(line)
    }
  }
}

object LogicManagerLive {
  val layer: ZLayer[Any, Nothing, LogicManager] =
    ZLayer.fromZIO(UIO.succeed(LogicManagerLive()))
}
