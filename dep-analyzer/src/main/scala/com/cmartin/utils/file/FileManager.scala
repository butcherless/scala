package com.cmartin.utils.file

import com.cmartin.learn.common.Utils._
import com.cmartin.utils.model.Domain
import com.cmartin.utils.model.Domain._
import zio._

import scala.io.{BufferedSource, Source}

case class FileManager()
    extends IOManager {

  override def getLinesFromFile(filename: String): IO[DomainError, List[String]] =
    ZIO.scoped {
      scopedFile(filename).flatMap { file =>
        ZIO.logInfo(s"reading from file: $filename") *>
          ZIO.attempt(file.getLines().toList)
      }.orElseFail(FileIOError(s"${Domain.OPEN_FILE_ERROR}: $filename"))
    }
  override def logWrongDependencies(errors: Iterable[DomainError]): Task[Unit]   =
    ZIO.foreachDiscard(errors)(e => ZIO.logInfo(s"invalid dependency: $e"))

  override def logPairCollection(collection: Iterable[GavPair]): UIO[Iterable[String]] = {
    Task.succeed(
      collection
        .filter(_.hasNewVersion)
        .map(formatChanges)
    )
  }

  /*
    H E L P E R S
   */

  def scopedFile(filename: String): RIO[Scope, BufferedSource] =
    ZIO.fromAutoCloseable(Task.attempt(Source.fromFile(filename)))

  def formatChanges(pair: Domain.GavPair): String =
    s"${pair.local.formatShort} ${colourGreen("=>")} ${colourBlue(pair.remote.version)}"

}

object FileManager {
  val layer: ULayer[IOManager] =
    ZLayer.succeed(FileManager())
}
