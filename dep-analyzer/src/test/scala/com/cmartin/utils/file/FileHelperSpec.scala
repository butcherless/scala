package com.cmartin.utils.file

import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{FileIOError, Gav, UnknownError}
import com.cmartin.utils.file.FileHelper.FileLines
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.{Runtime, FiberFailure, ZIO}

class FileHelperSpec extends AnyFlatSpec with Matchers {

  import FileHelperSpec._

  val runtime = Runtime.default

  behavior of "FileHelper"

  it should "return a non empty list of lines" in {
    val filename = "dep-analyzer/src/test/resources/test-file.txt"

    val program: ZIO[FileHelper, Domain.DomainError, FileLines] = for {
      lines <- FileHelper.>.getLinesFromFile(filename)
    } yield lines

    // provide ~ dependency injection
    val result: FileLines = runtime.unsafeRun(program.provide(FileHelperLive))
    result shouldBe expectedLines
  }

  it should "return a file domain error" in {
    val filename = "non-existent-file"

    val program: ZIO[FileHelper, Domain.DomainError, FileLines] = for {
      lines <- FileHelper.>.getLinesFromFile(filename)
    } yield lines

    val failure = the[FiberFailure] thrownBy runtime.unsafeRun(program.provide(FileHelperLive))

    failure.cause.failureOption.map { message => message shouldBe FileIOError(Domain.OPEN_FILE_ERROR) }
  }

  it should "return an unknown domain error" in {
    val filename = "unknown"
    val program = for {
      result <- FileHelperTest.fileHelper.getLinesFromFile(filename)
    } yield result

    val failure = the[FiberFailure] thrownBy runtime.unsafeRun(program)

    failure.cause.failureOption.map { message => message shouldBe UnknownError(UNKNOWN_ERROR_MESSAGE) }
  }

  it should "write two lines in the log destination" in {
    //TODO https://gist.github.com/jdegoes/dd66656382247dc5b7228fb0f2cb97c8
    // add a logger dependency, via constructor or via module pattern
    val deps    = Seq(Left("invalid.dep"), Right(Gav("group", "artifact", "version")))
    val program = FileHelper.>.logDepCollection(deps)

    info("TODO: refactor log module")
    runtime.unsafeRun(program.provide(FileHelperLive))
  }

}

object FileHelperSpec {
  val expectedLines: Seq[String] = Seq("line-1", "line-2")
  val FILE_NOT_FOUD_MESSAGE      = "file not found"
  val UNKNOWN_ERROR_MESSAGE      = "error trying to access a file"
}
