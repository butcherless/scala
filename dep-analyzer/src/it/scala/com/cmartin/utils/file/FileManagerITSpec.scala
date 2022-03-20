package com.cmartin.utils.file

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.model.Domain
import com.cmartin.utils.model.Domain.FileIOError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.{Runtime, ZEnv}

class FileManagerITSpec
    extends AnyFlatSpec
    with Matchers {

  behavior of "FileManager"
  // val loggers = ZLogger.Set.default.map(println(_)).filterLogLevel(_ >= LogLevel.Debug)

  val runtime: Runtime[ZEnv] =
    Runtime.default
      .mapRuntimeConfig(ConfigHelper.logAspect)

  it should "retrieve a sequence of text lines from a file" in {
    val filename = "dep-analyzer/src/it/resources/dependency-list.txt"
    val program  = IOManager(_.getLinesFromFile(filename))
      .provide(FileManager.layer)

    val lines = runtime.unsafeRun(program)

    lines.nonEmpty shouldBe true
    lines shouldBe List("line-1", "line-2")
  }

  it should "fail for missing file" in {
    val filename = "missing-file.txt"
    val program  = IOManager(_.getLinesFromFile(filename))
      .provide(FileManager.layer)

    val lines = runtime.unsafeRun(program.either)

    lines shouldBe Left(FileIOError(s"${Domain.OPEN_FILE_ERROR}: $filename"))
  }

}
