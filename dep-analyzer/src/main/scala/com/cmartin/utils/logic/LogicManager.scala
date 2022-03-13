package com.cmartin.utils.logic

import com.cmartin.utils.model.Domain.Gav
import zio.{Accessible, UIO}

trait LogicManager {
  def parseLines(lines: List[String]): UIO[(Iterable[String], Iterable[Gav])]

  def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]]

  def excludeFromList(dependencies: Iterable[Gav], exclusions: List[String]): UIO[Iterable[Gav]]

  def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double]

}

object LogicManager
    extends Accessible[LogicManager] {}
