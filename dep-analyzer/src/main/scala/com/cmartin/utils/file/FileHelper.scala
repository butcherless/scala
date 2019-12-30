package com.cmartin.utils.file

import com.cmartin.utils.Domain.{DomainError, Gav}
import zio.ZIO

trait FileHelper {
  val fileHelper: FileHelper.Service[Any]
}

object FileHelper {
  type FileLines        = Seq[String]
  type ParsedDependency = Either[String, Gav]

  /**
    * File operations
    * @tparam R the runtime required by the zio effect
    */
  trait Service[R] {

    /**
      * Retrieves a text line sequence from a text file
      * @param filename the name of the file containing the lines
      * @return a text line sequence
      */
    def getLinesFromFile(filename: String): ZIO[R, DomainError, FileLines]

    /**
      * Logs a parsed dependency sequence. Either valid or invalid.
      * @param dependencies parsed dependency sequence
      * @return Unit
      */
    def logDepCollection(dependencies: Seq[Either[String, Gav]]): ZIO[R, DomainError, Unit]

  }

  object > extends FileHelper.Service[FileHelper] {
    override def getLinesFromFile(filename: String): ZIO[FileHelper, DomainError, FileLines] =
      ZIO.accessM(r => r.fileHelper getLinesFromFile filename)

    override def logDepCollection(dependencies: Seq[Either[String, Gav]]): ZIO[FileHelper, DomainError, Unit] =
      ZIO.accessM(r => r.fileHelper logDepCollection dependencies)
  }
}
