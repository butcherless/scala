package com.cmartin.utils.file

import com.cmartin.learn.common.Utils._
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{DomainError, FileIOError, Gav, RepoResult}
import zio._

import scala.io.{BufferedSource, Source}

case class FileManagerLive()
    extends FileManager {

  override def getLinesFromFile(filename: String): IO[DomainError, List[String]] =
    manageFile(filename).use { file =>
      ZIO.logInfo(s"reading from file: $filename") *>
        ZIO.attempt(file.getLines().toList)
    }.orElseFail(FileIOError(s"${Domain.OPEN_FILE_ERROR}: $filename"))

  override def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit] = {
    Task.attempt(
      dependencies.foreach { dep =>
        dep.fold(
          line =>
            ZIO.logInfo(s"${colourRed("invalid dependency")} => ${colourRed(line)}"),
          dep => ZIO.logInfo(dep.toString)
        )
      }
    )
  }

  override def logPairCollection(collection: Iterable[RepoResult[Domain.GavPair]]): Task[Unit] = {
    Task.succeed {
      collection.foreach(
        _.fold(
          error => ZIO.logInfo(error.toString),
          pair => if (pair.hasNewVersion) ZIO.logInfo(formatChanges(pair))
        )
      )
    }
  }

  /*
    H E L P E R S
   */

  def manageFile(filename: String): TaskManaged[BufferedSource] =
    ZManaged.fromAutoCloseable(Task.attempt(Source.fromFile(filename)))

  def formatChanges(pair: Domain.GavPair): String =
    s"${pair.local.formatShort} ${colourGreen("=>")} ${colourBlue(pair.remote.version)}"

}

object FileManagerLive {
  val layer: ULayer[FileManager] =
    ZLayer.fromZIO(UIO.succeed(FileManagerLive()))
}
