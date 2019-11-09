package com.cmartin.utils.environment

import java.io.{File, FileInputStream}

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.learn.common.Utils.colourRed
import com.cmartin.utils.Domain.Gav
import zio.{Task, UIO}

import scala.io.BufferedSource

trait FileManagerLive extends FileManager with ComponentLogging {

  val fileManager = new FileManager.Service[Any] {
    override def getLinesFromFile(filename: String): Task[List[String]] =
      for {
        fis   <- openFile(filename)
        lines <- Task(new BufferedSource(fis)).bracket(closeSource)(getLines)
      } yield lines.toList

    override def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit] = {
      Task(
        dependencies.foreach {
          case Left(line) =>
            log.info(s"${colourRed("invalid dependency")} => ${colourRed(line)}") //TODO create printRed helper function
          case Right(dep) => log.info(dep.toString)
        }
      )
    }
  }

  /*
  H E L P E R S
   */

  private def openFile(filename: String): Task[FileInputStream] =
    Task(
      new FileInputStream(new File(filename))
    )

  private def closeSource(source: BufferedSource): UIO[Unit] = {
    UIO(
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
