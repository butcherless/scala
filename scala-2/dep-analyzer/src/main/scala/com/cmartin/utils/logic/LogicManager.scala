package com.cmartin.utils.logic

import com.cmartin.utils.Domain.Gav
import zio.Accessible
import zio.UIO

trait LogicManager {
  def parseLines(lines: List[String]): UIO[List[Either[String, Gav]]]

  def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]]

  def excludeList(dependencies: List[Gav], exclusionList: List[String]): UIO[List[Gav]]

  def calculateValidRate(dependencyCount: Int, validCount: Int): UIO[Double]
}

object LogicManager
    extends Accessible[LogicManager] {}
