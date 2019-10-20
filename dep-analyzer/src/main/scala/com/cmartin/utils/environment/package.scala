package com.cmartin.utils

import com.cmartin.utils.Domain.Gav
import zio.ZIO

package object environment {

  def getLinesFromFile(filename: String): ZIO[FileManager, Throwable, List[String]] =
    ZIO.accessM(_.manager getLinesFromFile filename)

  def parseLines(lines: List[String]): ZIO[FileManager, Nothing, List[Either[String, Domain.Gav]]] =
    ZIO.accessM(_.manager parseLines lines)

  def logDepCollection(dependencies: List[Either[String, Gav]]): ZIO[FileManager, Throwable, Unit] =
    ZIO.accessM(_.manager logDepCollection dependencies)

  def filterValid(dependencies: List[Either[String, Gav]]): ZIO[FileManager, Nothing, List[Gav]] =
    ZIO.accessM(_.manager filterValid dependencies)

  def excludeList(dependencies: List[Gav], exclusionList: List[String]): ZIO[FileManager, Nothing, List[Gav]] =
    ZIO.accessM(_.manager excludeList(dependencies, exclusionList))
}
