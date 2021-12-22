package com.cmartin.utils.file

import com.cmartin.utils.Domain.{DomainError, Gav, GavPair, RepoResult}
import zio.{Accessible, IO, Task}

trait FileManager {
  def getLinesFromFile(filename: String): IO[DomainError, List[String]]

  def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit]

  def logPairCollection(collection: Iterable[RepoResult[GavPair]]): Task[Unit]

}

object FileManager
    extends Accessible[FileManager]
