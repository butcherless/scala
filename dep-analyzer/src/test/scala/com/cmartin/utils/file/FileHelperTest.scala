package com.cmartin.utils.file

import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{FileIOError, UnknownError}
import com.cmartin.utils.file.FileHelper.FileLines
import com.cmartin.utils.file.FileHelperSpec._
import zio.{IO, UIO, ZIO}

trait FileHelperTest extends FileHelper {

  override val fileHelper: FileHelper.Service[Any] = new FileHelper.Service[Any] {

    override def getLinesFromFile(filename: String): IO[Domain.DomainError, FileLines] = {
      filename match {
        case ""         => IO.fail(FileIOError(FILE_NOT_FOUD_MESSAGE))
        case "filename" => UIO(expectedLines)
        case _          => IO.fail(UnknownError(UNKNOWN_ERROR_MESSAGE))
      }
    }

    override def logDepCollection(dependencies: Seq[Either[String, Domain.Gav]]): ZIO[Any, Domain.DomainError, Unit] = ???

  }
}

object FileHelperTest extends FileHelperTest
