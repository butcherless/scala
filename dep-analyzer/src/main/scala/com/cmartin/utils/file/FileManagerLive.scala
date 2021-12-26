package com.cmartin.utils.file

import com.cmartin.learn.common.Utils._
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain._
import zio._

import scala.io.{BufferedSource, Source}

case class FileManagerLive()
    extends FileManager {

  override def getLinesFromFile(filename: String): IO[DomainError, List[String]] =
    manageFile(filename).use { file =>
      ZIO.logLevel(LogLevel.Debug) {
        ZIO.log(s"reading from file: $filename")
      } *>
        ZIO.attempt(file.getLines().toList)
    }.orElseFail(FileIOError(s"${Domain.OPEN_FILE_ERROR}: $filename"))

  override def logWrongDependencies(dependencies: List[String]): Task[Unit] =
    ZIO.foreachDiscard(dependencies)(d => ZIO.logInfo(s"invalid dependency: $d"))

  override def logPairCollection(collection: Iterable[GavPair]): Task[Unit] = {
    ZIO.foreachDiscard(collection) { pair =>
      ZIO.when(pair.hasNewVersion)(ZIO.logInfo(formatChanges(pair)))
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
