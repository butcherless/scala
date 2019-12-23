package com.cmartin.utils.file
import com.cmartin.utils.Domain
import com.cmartin.utils.file.FileHelper.FileLines
import zio.{UIO, ZIO}

trait FileHelperLive extends FileHelper {
  override val fileHelper: FileHelper.Service[Any] = new FileHelper.Service[Any] {
    override def getLinesFromFile(filename: String): ZIO[Any, Domain.DomainError, FileLines] = {
      UIO.effectTotal(Seq("line-1", "line-2"))
    }
  }
}
