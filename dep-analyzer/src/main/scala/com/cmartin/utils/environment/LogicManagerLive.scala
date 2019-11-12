package com.cmartin.utils.environment

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.Gav
import zio.{UIO, ZIO}

import scala.util.matching.Regex

trait LogicManagerLive extends LogicManager with ComponentLogging {
  val pattern: Regex =
    raw"(^[a-z][a-z0-9-_\.]+):([a-zA-Z0-9-_\.]+):([0-9A-Za-z-\.]+)".r

  val logicManager: LogicManager.Service[Any] = new LogicManager.Service[Any] {
    override def parseLines(lines: List[String]): ZIO[Any, Nothing, List[Either[String, Domain.Gav]]] =
      UIO.foreach(lines)(line => UIO(parseDepLine(line)))

    override def filterValid(dependencies: List[Either[String, Domain.Gav]]): ZIO[Any, Nothing, List[Domain.Gav]] =
      UIO.effectTotal(
        dependencies
          .collect {
            case Right(dep) => dep
          }
      )

    override def excludeList(
                              dependencies: List[Domain.Gav],
                              exclusionList: List[String]
                            ): ZIO[Any, Nothing, List[Domain.Gav]] =
      UIO.effectTotal(
        dependencies.filterNot(dep => exclusionList.contains(dep.group))
      )

    override def calculateValidRate(dependencyCount: Int, validCount: Int): ZIO[Any, Nothing, Double] =
      UIO.effectTotal(100.toDouble * validCount / dependencyCount)
  }

  /*
    H E L P E R S
   */
  private def parseDepLine(line: String): Either[String, Gav] = {
    log.debug(s"reading dependency candidate: $line matches regex? $matches")
    lazy val matches = pattern.matches(line)

    if (matches) {
      val regexMatch: Regex.Match = pattern.findAllMatchIn(line).next()
      Right(
        Gav(
          regexMatch.group(1), // group
          regexMatch.group(2), // artifact
          regexMatch.group(3) // version
        )
      )
    } else {
      Left(line)
    }
  }
}

object LogicManagerLive extends LogicManagerLive
