package com.cmartin.zio

import com.cmartin.utils.ZioLearn.{MyDomainException, MyExceptionTwo}
import org.scalatest.{FlatSpec, Matchers}
import zio.{DefaultRuntime, FiberFailure, IO, Task, ZIO}

class ZioLearnSpec
  extends FlatSpec
    with Matchers
    with DefaultRuntime {


  "A Task" should "throw a FiberFailure" in {
    val program = for {
      a <- Task(1)
      b <- Task(1 / 0)
    } yield b

    an[FiberFailure] should be thrownBy unsafeRun(program)
  }

  it should "should return a Left containing an exception" in {
    val program = for {
      a <- Task(1)
      b <- Task(1 / 0)
    } yield b

    val result = unsafeRun(program.either)
    result.isLeft shouldBe true
    result.swap.map(e => assert(e.isInstanceOf[ArithmeticException] == true))
  }

  //  "A Task" should "DELETE-ME refine a failure" in {
  //    val program = for {
  //      a <- Task(1)
  //      b <- Task(1 / 0).refineOrDie {
  //        //case e: java.lang.ArithmeticException => e
  //        case _: Throwable => 0
  //      }
  //    } yield b
  //
  //
  //    val result = unsafeRun(program)
  //    val stop = 0
  //  }

  // catch some exceptions or die,


  it should "refine a failure" in {
    import com.cmartin.utils.ZioLearn.refineError

    val program: Task[Int] = for {
      a <- Task.effect(1 + 1)
      b <- Task.effect(a / 0)
    } yield b

    val programRefined: IO[MyDomainException, Int] = program.refineOrDie(refineError())

    val result = unsafeRun(programRefined.either)

    result.swap.map { e =>
      assert(e.isInstanceOf[MyExceptionTwo])
    }
  }

  it should "map an error from None to String" in {
    val none: Option[Int] = None
    val noneZio: IO[Unit, Int] = ZIO.fromOption(none)
    val program: IO[String, Int] = noneZio.mapError(_ => "mapped error")

    an[FiberFailure] should be thrownBy unsafeRun(program)
  }

  it should "return a Left with an string error" in {
    val none: Option[Int] = None
    val noneZio: IO[Unit, Int] = ZIO.fromOption(none)
    val program: IO[String, Int] = noneZio.mapError(_ => "mapped error")

    val result: Either[String, Int] = unsafeRun(program.either)

    result shouldBe Left("mapped error")
  }

}
