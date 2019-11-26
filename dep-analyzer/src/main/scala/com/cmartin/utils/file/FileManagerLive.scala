package com.cmartin.utils.file

import java.io.{File, FileInputStream}

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils.{colourBlue, colourGreen, colourRed}
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{Gav, RepoResult}
import zio.{Task, UIO}

import scala.io.BufferedSource

trait FileManagerLive extends FileManager with ComponentLogging {
  val fileManager = new FileManager.Service[Any] {
    override def getLinesFromFile(filename: String): Task[List[String]] =
      for {
        fis <- openFile(filename)
        lines <- createFileSource(fis).bracket(closeSource)(getLines)
      } yield lines.toList

    override def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit] = {
      Task.effect(
        dependencies.foreach { dep =>
          dep.fold(line => log.info(s"${colourRed("invalid dependency")} => ${colourRed(line)}"), (dep => log.info(dep.toString)))
        }
      )
    }

    override def logMessage(message: String): Task[Unit] = {
      Task.effect(
        log.info(message)
      )
    }

    override def logPairCollection(collection: List[RepoResult[Domain.GavPair]]): Task[Unit] = {
      Task.effectTotal {
        collection.foreach(
          _.fold(error => log.info(error.toString), pair => if (pair.hasNewVersion()) log.info(formatChanges(pair)))
        )
      }
    }
  }

  /*
  H E L P E R S
   */
  def formatChanges(pair: Domain.GavPair): String =
    s"${pair.local.formatShort} ${colourGreen("=>")} ${colourBlue(pair.remote.version)}"

  private def openFile(filename: String): Task[FileInputStream] =
    Task.effect(
      new FileInputStream(new File(filename))
    )

  private def createFileSource(fis: FileInputStream): Task[BufferedSource] = {
    Task.effect(
      new BufferedSource(fis)
    )
  }

  private def closeSource(source: BufferedSource): UIO[Unit] = {
    UIO.effectTotal(
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

object FileManagerLive extends FileManagerLive
