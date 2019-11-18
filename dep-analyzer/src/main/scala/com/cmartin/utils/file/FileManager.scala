package com.cmartin.utils.file

import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import zio.ZIO

trait FileManager {
  val fileManager: FileManager.Service[Any]
}

object FileManager {

  trait Service[R] {
    def getLinesFromFile(filename: String): ZIO[R, Throwable, List[String]]

    def logDepCollection(dependencies: List[Either[String, Gav]]): ZIO[R, Throwable, Unit]

    def logMessage(message: String): ZIO[R, Throwable, Unit]

    def logPairCollection(collection: List[RepoResult[GavPair]]): ZIO[R, Throwable, Unit]
  }

}
