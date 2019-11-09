package com.cmartin.utils.environment

import com.cmartin.utils.Domain.Gav
import zio.ZIO

trait LogicManager {
  val logicManager: LogicManager.Service[Any]
}

object LogicManager {
  trait Service[R] {
    def parseLines(lines: List[String]): ZIO[R, Nothing, List[Either[String, Gav]]]

    def filterValid(dependencies: List[Either[String, Gav]]): ZIO[R, Nothing, List[Gav]]

    def excludeList(dependencies: List[Gav], exclusionList: List[String]): ZIO[R, Nothing, List[Gav]]

    def calculateValidRate(dependencyCount: Int, validCount: Int): ZIO[R, Nothing, Double]
  }
}
