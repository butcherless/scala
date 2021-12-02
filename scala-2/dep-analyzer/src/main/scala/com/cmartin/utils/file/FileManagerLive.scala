package com.cmartin.utils.file

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils._
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.Gav
import com.cmartin.utils.Domain.RepoResult
import zio._

import java.io.File
import java.io.FileInputStream
import scala.io.BufferedSource

case class FileManagerLive()
    extends FileManager
    with ComponentLogging {

  override def getLinesFromFile(filename: String): Task[List[String]] =
    for {
      fis <- openFile(filename)
      source <- createFileSource(fis) // TODO use ZManaged
      lines <- getLines(source)
    } yield lines.toList

  override def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit] = {
    Task.attempt(
      dependencies.foreach { dep =>
        dep.fold(
          line =>
            log.info(
              s"${colourRed("invalid dependency")} => ${colourRed(line)}"
            ),
          dep => log.info(dep.toString)
        )
      }
    )
  }

  override def logMessage(message: String): Task[Unit] = {
    Task.attempt(
      log.info(message)
    )
  }

  override def logPairCollection(collection: List[RepoResult[Domain.GavPair]]): Task[Unit] = {
    Task.succeed {
      collection.foreach(
        _.fold(
          error => log.info(error.toString),
          pair => if (pair.hasNewVersion) log.info(formatChanges(pair))
        )
      )
    }
  }

  /*
    H E L P E R S
   */
  def formatChanges(pair: Domain.GavPair): String =
    s"${pair.local.formatShort} ${colourGreen("=>")} ${colourBlue(pair.remote.version)}"

  private def openFile(filename: String): Task[FileInputStream] =
    Task.attempt(
      new FileInputStream(new File(filename))
    )

  private def createFileSource(fis: FileInputStream): Task[BufferedSource] = {
    Task.attempt(
      new BufferedSource(fis)
    )
  }

  private def closeSource(source: BufferedSource): UIO[Unit] = {
    UIO.succeed(
      source
        .close()
    )
  }

  private def getLines(source: BufferedSource): Task[Iterator[String]] =
    Task(
      source
        .getLines()
    )
}

object FileManagerLive {
  val layer: ZLayer[Any, Nothing, FileManager] =
    ZLayer.fromZIO(UIO.succeed(FileManagerLive()))
}
