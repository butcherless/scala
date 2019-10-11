package com.cmartin.utils.environment

import java.io.{File, FileInputStream}

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain.Gav
import zio.{Task, UIO}

import scala.io.BufferedSource
import scala.util.matching.Regex

trait FileManagerEnv {
  def fileManagerEnv: FileManagerEnv.Service
}

object FileManagerEnv {

  trait Service {
    def getLinesFromFile(filename: String): Task[List[String]]

    def parseDepLine(line: String): Either[String, Gav]
  }

  trait Live
    extends FileManagerEnv.Service
      with ComponentLogging {

    val pattern = raw"(^[a-z][a-z0-9-_\.]+):([a-z0-9-_\.]+):([0-9A-Za-z-\.]+)".r

    override def getLinesFromFile(filename: String): Task[List[String]] = for {
      fis <- openFile(filename)
      lines <- Task(new BufferedSource(fis)).bracket(closeSource)(getLines)
    } yield lines.toList

    override def parseDepLine(line: String): Either[String, Gav] = {
      val matches = pattern.matches(line)
      log.debug(s"reading dependency candidate: $line matches regex? $matches")

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

  object Live extends Live {
  }

}

