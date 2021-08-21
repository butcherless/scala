package com.cmartin.zio

import com.cmartin.utils.file.{FileManager, FileManagerLive}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio._

class FileManagerSpec extends AnyFlatSpec with Matchers {

  val runtime = Runtime.default

  behavior of "FileManager"

  it should "provide the env" in {
    // GIVEN
    val program: ZIO[FileManager, Throwable, Unit] = for {
      _ <- FileManager.>.logMessage(">>> message <<<")
    } yield ()

    val io = program.provideSome[Any] { f => FileManagerLive }

    // WHEN
    val r = runtime.unsafeRun(io)

    // THEN
    r shouldBe ()
  }

  it should "repeat a message" in {
    import zio.Duration
    val policy1 = Schedule
      .exponential(10.milliseconds)
      .tapOutput(o => UIO(println(o))) >>> (Schedule.recurWhile(
      _ < 2.second
    ))

    val policy2 = Schedule.recurs(5) || Schedule.recurs(10)
    val program =
      Task.attempt(println("zio schedule test")) repeat policy1

    //TODO runtime.unsafeRun(program)
  }
}
