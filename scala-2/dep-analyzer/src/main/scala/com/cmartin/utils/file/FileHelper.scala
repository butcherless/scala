package com.cmartin.utils.file

import com.cmartin.utils.Domain.DomainError
import com.cmartin.utils.Domain.Gav
import zio.Accessible
import zio.IO

import FileHelper.FileLines

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

  type FileLines = Seq[String]
  type ParsedDependency = Either[String, Gav]
}
