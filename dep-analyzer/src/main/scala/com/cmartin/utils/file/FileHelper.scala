package com.cmartin.utils.file

import com.cmartin.utils.Domain.DomainError
import zio.ZIO

trait FileHelper {
  val fileHelper: FileHelper.Service[Any]
}

object FileHelper {
  type FileLines = Seq[String]

  trait Service[R] {
    def getLinesFromFile(filename: String): ZIO[R, DomainError, FileLines]
  }

  object > extends FileHelper.Service[FileHelper] {
    override def getLinesFromFile(filename: String): ZIO[FileHelper, DomainError, FileLines] =
      ZIO.accessM(r => r.fileHelper getLinesFromFile filename)
  }
}
