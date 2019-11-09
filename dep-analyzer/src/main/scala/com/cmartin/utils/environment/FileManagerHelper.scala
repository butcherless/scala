package com.cmartin.utils.environment
import com.cmartin.utils.Domain
import zio.ZIO

object FileManagerHelper extends FileManager.Service[FileManager] {
  override def getLinesFromFile(
      filename: String
  ): ZIO[FileManager, Throwable, List[String]] =
    ZIO.accessM(_.manager getLinesFromFile filename)

  override def parseLines(
      lines: List[String]
  ): ZIO[FileManager, Nothing, List[Either[String, Domain.Gav]]] =
    ZIO.accessM(_.manager parseLines lines)

  override def filterValid(
      dependencies: List[Either[String, Domain.Gav]]
  ): ZIO[FileManager, Nothing, List[Domain.Gav]] = ZIO.accessM(_.manager filterValid dependencies)

  override def logDepCollection(
      dependencies: List[Either[String, Domain.Gav]]
  ): ZIO[FileManager, Throwable, Unit] = ZIO.accessM(_.manager logDepCollection dependencies)

  override def excludeList(
      dependencies: List[Domain.Gav],
      exclusionList: List[String]
  ): ZIO[FileManager, Nothing, List[Domain.Gav]] =
    ZIO.accessM(_.manager excludeList (dependencies, exclusionList))
}
