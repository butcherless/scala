package com.cmartin.utils.domain

import Model.{DomainError, GavPair}
import zio.{IO, UIO, URIO, ZIO}

trait IOManager {
  def getLinesFromFile(filename: String): IO[DomainError, List[String]]

  def logWrongDependencies(dependencies: Iterable[DomainError]): UIO[Unit]

  def logPairCollection(collection: Iterable[GavPair]): UIO[Iterable[String]]

}

object IOManager {
  def getLinesFromFile(filename: String): ZIO[IOManager, DomainError, List[String]] =
    ZIO.serviceWithZIO[IOManager](_.getLinesFromFile(filename))

  def logWrongDependencies(dependencies: Iterable[DomainError]): URIO[IOManager, Unit] =
    ZIO.serviceWithZIO[IOManager](_.logWrongDependencies(dependencies))

  def logPairCollection(collection: Iterable[GavPair]): URIO[IOManager, Iterable[String]] =
    ZIO.serviceWithZIO[IOManager](_.logPairCollection(collection))
}
