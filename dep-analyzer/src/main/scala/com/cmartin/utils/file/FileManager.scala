package com.cmartin.utils.file

import com.cmartin.utils.Domain
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

  object Helper extends FileManager.Service[FileManager] {
    override def getLinesFromFile(filename: String): ZIO[FileManager, Throwable, List[String]] =
      ZIO.accessM(_.fileManager getLinesFromFile filename)

    override def logDepCollection(dependencies: List[Either[String, Domain.Gav]]): ZIO[FileManager, Throwable, Unit] =
      ZIO.accessM(_.fileManager logDepCollection dependencies)

    override def logMessage(message: String): ZIO[FileManager, Throwable, Unit] = {
      ZIO.accessM(_.fileManager logMessage message)
    }

    override def logPairCollection(collection: List[RepoResult[Domain.GavPair]]): ZIO[FileManager, Throwable, Unit] = {
      ZIO.accessM(_.fileManager logPairCollection collection)
    }
  }

}
