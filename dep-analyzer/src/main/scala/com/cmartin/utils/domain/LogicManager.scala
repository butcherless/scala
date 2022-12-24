package com.cmartin.utils.domain

import com.cmartin.utils.domain.LogicManager.ParsedLines
import Model.Gav
import zio.{UIO, URIO, ZIO}

trait LogicManager {
  def parseLines(lines: List[String]): UIO[ParsedLines]

  def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]]

  def excludeFromList(dependencies: Iterable[Gav], exclusions: List[String]): UIO[Iterable[Gav]]

  def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double]

}

object LogicManager {
  final case class ParsedLines(
      failedList: Iterable[String],
      successList: Iterable[Gav]
  )

  def parseLines(lines: List[String]): URIO[LogicManager, ParsedLines] =
    ZIO.serviceWithZIO[LogicManager](_.parseLines(lines))

  def filterValid(dependencies: List[Either[String, Gav]]): URIO[LogicManager, List[Gav]] =
    ZIO.serviceWithZIO[LogicManager](_.filterValid(dependencies))

  def excludeFromList(dependencies: Iterable[Gav], exclusions: List[String]): URIO[LogicManager, Iterable[Gav]] =
    ZIO.serviceWithZIO[LogicManager](_.excludeFromList(dependencies, exclusions))

  def calculateValidRate(dependencyCount: Int, validCount: Int): URIO[LogicManager, Double] =
    ZIO.serviceWithZIO[LogicManager](_.calculateValidRate(dependencyCount, validCount))

}
