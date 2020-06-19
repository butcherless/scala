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

  it should "do something more" in {
    trait DomainError
    case class DomainErrorOne(msg: String) extends DomainError

    val r: Task[Int] =
      Task
        .effect(1 / 2)

    val de: IO[DomainError, Int] =
      Task
        .effect(1 / 0)
        .mapError(_ => DomainErrorOne("arithmetic error"))
    // GIVEN

    // WHEN

    // THEN
  }

  it should "repeat a message" in {
    import zio.duration._
    val policy1 = Schedule.exponential(10.milliseconds).tapOutput(o => UIO(println(o))) >>> (Schedule.doWhile(
      _ < 2.second
    ))

    val policy2 = Schedule.recurs(5) || Schedule.recurs(10)
    val program =
      Task.effect(println("zio schedule test")) repeat policy1

    //TODO runtime.unsafeRun(program)
  }
}
