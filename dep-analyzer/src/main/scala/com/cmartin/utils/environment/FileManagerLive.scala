package com.cmartin.utils.environment

import java.io.{File, FileInputStream}

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.Gav
import zio.{Task, UIO, ZIO}

import scala.io.BufferedSource
import scala.util.matching.Regex

trait FileManagerLive
  extends FileManager
    with ComponentLogging {

  val pattern = raw"(^[a-z][a-z0-9-_\.]+):([a-zA-Z0-9-_\.]+):([0-9A-Za-z-\.]+)".r

  val manager = new FileManager.Service {
    override def getLinesFromFile(filename: String): Task[List[String]] = for {
      fis <- openFile(filename)
      lines <- Task(new BufferedSource(fis)).bracket(closeSource)(getLines)
    } yield lines.toList

    override def parseLines(lines: List[String]): UIO[List[Either[String, Domain.Gav]]] =
      UIO.foreach(lines)(line => UIO(parseDepLine(line)))

    override def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]] =
      UIO(
        dependencies
          .collect {
            case Right(dep) => dep
          }
      )

    override def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit] = {
      Task(
        dependencies.foreach {
          case Left(line) => log.info(line)
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

  private def parseDepLine(line: String): Either[String, Gav] = {
    log.debug(s"reading dependency candidate: $line matches regex? $matches")
    lazy val matches = pattern.matches(line) //TODO check lazy val

    if (matches) {
      val regexMatch: Regex.Match = pattern.findAllMatchIn(line).next()
      Right(Gav(
        regexMatch.group(1), // group
        regexMatch.group(2), // artifact
        regexMatch.group(3)) // version
      )
    } else {
      Left(line)
    }
  }

}

object FileManagerLive extends FileManagerLive

object FileManagerHelper {
  def getLinesFromFile(filename: String): ZIO[FileManager, Throwable, List[String]] =
    ZIO.accessM(_.manager getLinesFromFile filename)
}