package com.cmartin.utils.file
import java.io.File
import java.io.FileInputStream

import scala.io.BufferedSource

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils.colourYellow
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.DomainError
import com.cmartin.utils.Domain.FileIOError
import com.cmartin.utils.file.FileHelper.FileLines
import zio.IO
import zio.Task
import zio.UIO

trait FileHelperLive extends FileHelper with ComponentLogging {

  override val fileHelper: FileHelper.Service[Any] =
    new FileHelper.Service[Any] {

      override def getLinesFromFile(
          filename: String
      ): IO[DomainError, FileLines] = {
        for {
          fis <- openFile(filename)
          lines <- createFileSource(fis)
            .acquireReleaseWith(closeSource)(getLines)
        } yield lines.toSeq
      }

      override def logDepCollection(
          dependencies: Seq[Either[String, Domain.Gav]]
      ): IO[DomainError, Unit] = {
        Task
          .attempt(
            dependencies
              .foreach { dep =>
                dep.fold(
                  line =>
                    log.info(
                      s"${colourYellow("invalid dependency")} => ${colourYellow(line)}"
                    ),
                  dep => log.info(dep.toString) // OK case
                )
              }
          )
          .orElseFail(FileIOError("Error writing a log message"))
      }
    }

  /*
    H E L P E R S
   */

  private def openFile(filename: String): IO[DomainError, FileInputStream] =
    Task
      .attempt(
        new FileInputStream(new File(filename))
      )
      .orElseFail(FileIOError(Domain.OPEN_FILE_ERROR))

  private def createFileSource(
      fis: FileInputStream
  ): IO[DomainError, BufferedSource] = {
    Task
      .attempt(
        new BufferedSource(fis)
      )
      .orElseFail(FileIOError(Domain.FILE_BUFFER_ERROR))
  }

  private def closeSource(source: BufferedSource): UIO[Unit] = {
    UIO.succeed(
      source
        .close()
    )
  }

  private def getLines(
      source: BufferedSource
  ): IO[DomainError, Iterator[String]] =
    Task(
      source
        .getLines()
    ).orElseFail(FileIOError("Error while accessing the file contents"))

}

object FileHelperLive extends FileHelperLive
