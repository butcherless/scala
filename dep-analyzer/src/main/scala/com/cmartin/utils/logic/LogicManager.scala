package com.cmartin.utils.logic

import com.cmartin.utils.Domain.Gav
import zio.{Accessible, UIO}

trait LogicManager {
  def parseLines(lines: List[String]): UIO[(Iterable[String], Iterable[Gav])]

  def parseLines2(lines: List[String]): UIO[List[Either[String, Gav]]]

  def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]]

  def excludeList(dependencies: Iterable[Gav], exclusionList: List[String]): UIO[Iterable[Gav]]

  def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double]

}

object LogicManager
    extends Accessible[LogicManager] {}
