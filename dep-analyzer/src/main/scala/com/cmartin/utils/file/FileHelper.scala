package com.cmartin.utils.file

import com.cmartin.utils.file.FileHelper.FileLines
import com.cmartin.utils.model.Domain.{DomainError, Gav}
import zio.{Accessible, IO}

trait FileHelper {

  /** Retrieves a text line sequence from a text file
    * @param filename
    *   the name of the file containing the lines
    * @return
    *   a text line sequence
    */
  def getLinesFromFile(filename: String): IO[DomainError, FileLines]

  /** Logs a parsed dependency sequence. Either valid or invalid.
    * @param dependencies
    *   parsed dependency sequence
    * @return
    *   Unit
    */
  def logDepCollection(dependencies: Seq[Either[String, Gav]]): IO[DomainError, Unit]

}

object FileHelper
    extends Accessible[FileHelper] {

  type FileLines        = Seq[String]
  type ParsedDependency = Either[String, Gav]
}
