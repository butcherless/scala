package com.cmartin.utils.logic

import com.cmartin.utils.model.Domain.Gav
import zio.Accessible
import zio.UIO

import LogicManager.ParsedLines

trait LogicManager {
  def parseLines(lines: List[String]): UIO[ParsedLines]

  def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]]

  def excludeFromList(dependencies: Iterable[Gav], exclusions: List[String]): UIO[Iterable[Gav]]

  def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double]

}

object LogicManager extends Accessible[LogicManager] {
  case class ParsedLines(
      failedList: Iterable[String],
      successList: Iterable[Gav]
  )

}
