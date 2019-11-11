package com.cmartin.utils.environment
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.RepoResult
import zio.ZIO

object FileManagerHelper extends FileManager.Service[FileManager] {
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
