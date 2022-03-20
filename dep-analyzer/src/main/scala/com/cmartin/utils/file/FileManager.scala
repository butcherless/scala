package com.cmartin.utils.file

import com.cmartin.utils.model.Domain.{DomainError, GavPair}
import zio.{Accessible, IO, Task, UIO}

trait FileManager {
  def getLinesFromFile(filename: String): IO[DomainError, List[String]]

  def logWrongDependencies(dependencies: Iterable[DomainError]): Task[Unit]

  def logPairCollection(collection: Iterable[GavPair]): UIO[Iterable[String]]

}

object FileManager extends Accessible[FileManager]
