package com.cmartin.utils.environment

import com.cmartin.utils.Domain.Gav
import zio.ZIO

trait FileManager {
  val manager: FileManager.Service[Any]
}

object FileManager {
  trait Service[R] {
    def getLinesFromFile(filename: String): ZIO[R, Throwable, List[String]]

    def parseLines(lines: List[String]): ZIO[R, Nothing, List[Either[String, Gav]]]

    def filterValid(dependencies: List[Either[String, Gav]]): ZIO[R, Nothing, List[Gav]]

    def logDepCollection(dependencies: List[Either[String, Gav]]): ZIO[R, Throwable, Unit]

    def excludeList(
        dependencies: List[Gav],
        exclusionList: List[String]
    ): ZIO[R, Nothing, List[Gav]]
  }
}
