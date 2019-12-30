package com.cmartin.utils.file
import java.io.{File, FileInputStream}

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils.colourRed
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{DomainError, FileIOError}
import com.cmartin.utils.file.FileHelper.FileLines
import zio.{IO, Task, UIO}

import scala.io.BufferedSource

trait FileHelperLive extends FileHelper with ComponentLogging {

  override val fileHelper: FileHelper.Service[Any] = new FileHelper.Service[Any] {

    override def getLinesFromFile(filename: String): IO[DomainError, FileLines] = {
      for {
        fis <- openFile(filename)
        lines <- createFileSource(fis)
          .bracket(closeSource)(getLines)
      } yield lines.toSeq
    }

    override def logDepCollection(dependencies: Seq[Either[String, Domain.Gav]]): IO[DomainError, Unit] = {
      Task
        .effect(
          dependencies
            .foreach { dep =>
              dep.fold(
                line => log.info(s"${colourRed("invalid dependency")} => ${colourRed(line)}"),
                dep => log.info(dep.toString) // OK case
              )
            }
        )
        .mapError(_ => FileIOError("Error writing a log message"))
    }
  }

  /*
    H E L P E R S
   */

  private def openFile(filename: String): IO[DomainError, FileInputStream] =
    Task
      .effect(
        new FileInputStream(new File(filename))
      )
      .mapError(_ => FileIOError(Domain.OPEN_FILE_ERROR))

  private def createFileSource(fis: FileInputStream): IO[DomainError, BufferedSource] = {
    Task
      .effect(
        new BufferedSource(fis)
      )
      .mapError(_ => FileIOError(Domain.FILE_BUFFER_ERROR))
  }

  private def closeSource(source: BufferedSource): UIO[Unit] = {
    UIO.effectTotal(
      source
        .close()
    )
  }

  private def getLines(source: BufferedSource): IO[DomainError, Iterator[String]] =
    Task(
      source
        .getLines()
    ).mapError(_ => FileIOError("Error while accessing the file contents"))

}

object FileHelperLive extends FileHelperLive
