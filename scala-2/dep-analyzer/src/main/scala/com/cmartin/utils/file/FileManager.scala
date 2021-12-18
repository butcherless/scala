package com.cmartin.utils.file

import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import zio.{Accessible, Task}

trait FileManager {
  def getLinesFromFile(filename: String): Task[List[String]]

  def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit]

  def logPairCollection(collection: List[RepoResult[GavPair]]): Task[Unit]

}

object FileManager
    extends Accessible[FileManager]
