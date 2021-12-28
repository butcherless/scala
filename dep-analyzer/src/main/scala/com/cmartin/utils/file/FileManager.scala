package com.cmartin.utils.file

import com.cmartin.utils.Domain.{DomainError, GavPair}
import zio.{Accessible, IO, Task}

trait FileManager {
  def getLinesFromFile(filename: String): IO[DomainError, List[String]]

  def logWrongDependencies(dependencies: Iterable[DomainError]): Task[Unit]

  def logPairCollection(collection: Iterable[GavPair]): Task[Unit]

}

object FileManager
    extends Accessible[FileManager]
