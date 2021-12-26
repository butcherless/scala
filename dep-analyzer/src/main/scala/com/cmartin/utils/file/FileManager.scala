package com.cmartin.utils.file

import com.cmartin.utils.Domain.{DomainError, GavPair, RepoResult}
import zio.{Accessible, IO, Task}

trait FileManager {
  def getLinesFromFile(filename: String): IO[DomainError, List[String]]

  def logWrongDependencies(dependencies: List[String]): Task[Unit]

  def logPairCollection(collection: Iterable[GavPair]): Task[Unit]

}

object FileManager
    extends Accessible[FileManager]
