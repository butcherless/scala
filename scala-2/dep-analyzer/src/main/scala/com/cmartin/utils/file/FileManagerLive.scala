package com.cmartin.utils.file

import com.cmartin.learn.common.Utils._
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{Gav, RepoResult}
import zio._

import java.io.{File, FileInputStream}
import scala.io.BufferedSource

case class FileManagerLive()
    extends FileManager {

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
            ZIO.logInfo(s"${colourRed("invalid dependency")} => ${colourRed(line)}"),
          dep => ZIO.logInfo(dep.toString)
        )
      }
    )
  }

  override def logPairCollection(collection: List[RepoResult[Domain.GavPair]]): Task[Unit] = {
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
