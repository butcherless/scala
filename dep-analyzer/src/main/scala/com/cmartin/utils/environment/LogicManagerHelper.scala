package com.cmartin.utils.environment
import com.cmartin.utils.Domain
import zio.ZIO

object LogicManagerHelper extends LogicManager.Service[LogicManager] {
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
    ZIO.accessM(_.logicManager excludeList (dependencies, exclusionList))

  override def calculateValidRate(dependencyCount: Int, validCount: Int): ZIO[LogicManager, Nothing, Double] =
    ZIO.accessM(_.logicManager calculateValidRate (dependencyCount, validCount))
}
