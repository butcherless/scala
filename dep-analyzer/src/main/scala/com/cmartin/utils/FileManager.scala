package com.cmartin.utils

import java.io.{File, FileInputStream}

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.DependencyLookoutApp.pattern
import com.cmartin.utils.Domain.Gav
import zio.{Task, UIO}

import scala.io.BufferedSource
import scala.util.matching.Regex

final object FileManager
  extends ComponentLogging {

  def getLinesFromFile(filename: String): Task[List[String]] = for {
    fis <- openFile(filename)
    lines <- Task(new BufferedSource(fis)).bracket(closeSource)(getLines)
  } yield lines.toList

  def parseDepLine(line: String) = {
    val matches = pattern.matches(line)
    log.debug(s"reading dependency candidate: $line matches regex? $matches")

    if (matches) {
      val regexMatch: Regex.Match = pattern.findAllMatchIn(line).next()
      Right(Gav(regexMatch.group(1), regexMatch.group(2), regexMatch.group(3)))
    } else {
      Left(line)
    }
  }

  def parseLines(lines: List[String]): UIO[List[Either[String, Gav]]] =
    UIO(lines.map(parseDepLine))

  def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]] =
    UIO(
      dependencies
        .collect {
          case Right(dep) => dep
        }
    )


  /*
    H E L P E R S
   */
  private def openFile(filename: String): Task[FileInputStream] =
    Task(
      new FileInputStream(new File(filename))
    )


  private def closeSource(source: BufferedSource): UIO[Unit] = {
    UIO(source.close())
  }


  private def getLines(source: BufferedSource): Task[Iterator[String]] =
    Task(
      source
        .getLines()
    )

}
