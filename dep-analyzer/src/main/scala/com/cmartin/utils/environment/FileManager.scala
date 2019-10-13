package com.cmartin.utils.environment

import com.cmartin.utils.Domain.Gav
import zio.{Task, UIO}

trait FileManager {
  val manager: FileManager.Service
}

object FileManager {

  trait Service {
    def getLinesFromFile(filename: String): Task[List[String]]
    def parseLines(lines: List[String]): UIO[List[Either[String, Gav]]]
    def filterValid(dependencies: List[Either[String, Gav]]): UIO[List[Gav]]
    def logDepCollection(dependencies: List[Either[String, Gav]]): Task[Unit]
  }

}

