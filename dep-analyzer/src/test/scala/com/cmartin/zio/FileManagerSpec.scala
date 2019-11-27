package com.cmartin.zio

import com.cmartin.utils.file.{FileManager, FileManagerLive}
import org.scalatest.{FlatSpec, Matchers}
import zio._

class FileManagerSpec extends FlatSpec with Matchers with DefaultRuntime {

  "SUT" should "provide the env" in {

    // GIVEN
    val program: ZIO[FileManager, Throwable, Unit] = for {
      _ <- FileManager.>.logMessage(">>> message <<<")
    } yield ()

    val io = program.provideSome[Any] { f =>
      FileManagerLive
    }

    // WHEN
    val r = unsafeRun(io)

    // THEN
    r shouldBe()
  }

  "SUT" should "do something more" in {
    // GIVEN

    // WHEN

    // THEN

  }


}
