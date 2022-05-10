package com.cmartin.zio

import com.cmartin.utils.file.{FileManager, IOManager}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio._

class FileManagerSpec
    extends AnyFlatSpec
    with Matchers {

  val runtime = Runtime.default

  behavior of "FileManager"

  it should "provide the env" in {
    // GIVEN
    val program: ZIO[IOManager, Throwable, Unit] = for {
      _ <- ZIO.logInfo(">>> message <<<")
    } yield ()

    val io = program.provide(FileManager.layer)

    // WHEN
    val r = runtime.unsafeRun(io)

    // THEN
    r shouldBe ()
  }

}
