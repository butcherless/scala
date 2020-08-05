package com.cmartin.utils.logic

import com.cmartin.utils.Domain
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

  object > extends LogicManager.Service[LogicManager] {
    override def parseLines(
        lines: List[String]
    ): ZIO[LogicManager, Nothing, List[Either[String, Domain.Gav]]] =
      ZIO.accessM(_.logicManager parseLines lines)

    override def filterValid(
        dependencies: List[Either[String, Domain.Gav]]
    ): ZIO[LogicManager, Nothing, List[Domain.Gav]] =
      ZIO.accessM(_.logicManager filterValid dependencies)

    override def excludeList(
        dependencies: List[Domain.Gav],
        exclusionList: List[String]
    ): ZIO[LogicManager, Nothing, List[Domain.Gav]] =
      ZIO.accessM(_.logicManager.excludeList(dependencies, exclusionList))

    override def calculateValidRate(dependencyCount: Int, validCount: Int): ZIO[LogicManager, Nothing, Double] =
      ZIO.accessM(_.logicManager.calculateValidRate(dependencyCount, validCount))
  }

}
