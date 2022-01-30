package com.cmartin.utils.file

import com.cmartin.learn.common.Utils.colourYellow
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{DomainError, FileIOError}
import com.cmartin.utils.file.FileHelper.FileLines
import zio._

import java.io.{File, FileInputStream}
import scala.io.BufferedSource

case class FileHelperLive()
    extends FileHelper {

  override def getLinesFromFile(filename: String): IO[DomainError, FileLines] = {
    for {
      _      <- ZIO.logInfo(s"getLinesFromFile: $filename")
      fis    <- openFile(filename)
      source <- createFileSource(fis)
      lines  <- getLines(source)
    } yield lines.toSeq
  }

  override def logDepCollection(dependencies: Seq[Either[String, Domain.Gav]]): IO[DomainError, Unit] = {
    Task.attempt(
      dependencies
        .foreach { dep =>
          dep.fold(
            line =>
              ZIO.logInfo(s"${colourYellow("invalid dependency")} => ${colourYellow(line)}"),
            dep => ZIO.logInfo(dep.toString) // OK case
          )
        }
    )
      .orElseFail(FileIOError("Error writing a log message"))
  }

  /*
    H E L P E R S
   */

  private def openFile(filename: String): IO[DomainError, FileInputStream] =
    Task.attempt(new FileInputStream(new File(filename)))
      .orElseFail(FileIOError(Domain.OPEN_FILE_ERROR))

  private def createFileSource(fis: FileInputStream): IO[DomainError, BufferedSource] = {
    Task.attempt(new BufferedSource(fis))
      .orElseFail(FileIOError(Domain.FILE_BUFFER_ERROR))
  }

  private def getLines(source: BufferedSource): IO[DomainError, Iterator[String]] =
    Task(source.getLines()).orElseFail(FileIOError("Error while accessing the file contents"))

}

object FileHelperLive extends (() => FileHelper) {
  val layer: ULayer[FileHelper] =
    FileHelperLive.toLayer
}
