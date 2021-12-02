package com.cmartin.utils.file

import com.cmartin.utils.Domain.Gav
import com.cmartin.utils.Domain.GavPair
import com.cmartin.utils.Domain.RepoResult
import zio.Accessible
import zio.Task

trait FileManager {
  def getLinesFromFile(filename: String): Task[List[String]]

  def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit]

  def logMessage(message: String): Task[Unit]

  def logPairCollection(collection: List[RepoResult[GavPair]]): Task[Unit]

}

object FileManager
    extends Accessible[FileManager]
