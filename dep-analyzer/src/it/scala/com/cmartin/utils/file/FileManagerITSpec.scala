package com.cmartin.utils.file

import com.cmartin.utils.Domain.FileIOError
import com.cmartin.utils.{Domain, Slf4jLogger}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.{Runtime, ZLogger}

class FileManagerITSpec
    extends AnyFlatSpec
      with Matchers {

  behavior of "FileManager"
  // val loggers = ZLogger.Set.default.map(println(_)).filterLogLevel(_ >= LogLevel.Debug)

  val loggers =
    ZLogger.Set.empty[String]
      .add(Slf4jLogger.defaultSlf4j)
      .add(ZLogger.defaultCause)

  val runtime = Runtime.default.mapRuntimeConfig(_.copy(loggers = loggers))

  it should "retrieve a sequence of text lines from a file" in {
    val filename = "dep-analyzer/src/it/resources/dependency-list.txt"
    val program  = FileManager(_.getLinesFromFile(filename))
      .provide(FileManagerLive.layer)

    val lines = runtime.unsafeRun(program)

    lines.nonEmpty shouldBe true
    lines shouldBe List("line-1", "line-2")
  }

  it should "fail for missing file" in {
    val filename = "missing-file.txt"
    val program  = FileManager(_.getLinesFromFile(filename))
      .provide(FileManagerLive.layer)

    val lines = runtime.unsafeRun(program.either)

    lines shouldBe Left(FileIOError(s"${Domain.OPEN_FILE_ERROR}: $filename"))
  }

}
