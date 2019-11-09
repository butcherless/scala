package com.cmartin.utils.environment

import com.cmartin.utils.Domain.Gav
import zio.ZIO

trait FileManager {
  val fileManager: FileManager.Service[Any]
}

object FileManager {
  trait Service[R] {
    def getLinesFromFile(filename: String): ZIO[R, Throwable, List[String]]
    def logDepCollection(dependencies: List[Either[String, Gav]]): ZIO[R, Throwable, Unit]
  }
}
