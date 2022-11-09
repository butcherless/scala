package com.cmartin.zio

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.config.ConfigHelper.AppConfig
import com.cmartin.utils.file.{FileManager, IOManager}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.{default => runtime}
import zio._

class FileManagerSpec
    extends AnyFlatSpec
    with Matchers {

  // val runtime = Runtime.default

  behavior of "FileManager"

  it should "provide the env" in {
    // GIVEN
    val program: ZIO[IOManager, Throwable, Unit] = for {
      _ <- ZIO.logInfo(">>> message <<<")
    } yield ()

    val io = program.provide(FileManager.layer)

    // WHEN

    val r = Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(io).getOrThrowFiberFailure()
    }

    // THEN
    r shouldBe ()
  }

  it should "read system property" in {
    val prog   = ConfigHelper.readFromEnv()
    val result = Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(prog).getOrThrowFiberFailure()
    }

    result shouldBe AppConfig("/tmp/dep-list.log", List("com.cmartin.learn", "com.cmartin.poc"))
  }

}
