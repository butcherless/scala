package com.cmartin.zio

import com.cmartin.utils.Domain.Gav
import com.cmartin.utils.ZioLearn.{MyDomainException, MyExceptionTwo}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio._

class ZioLearnSpec extends AnyFlatSpec with Matchers with DefaultRuntime {

  "A Task" should "throw a FiberFailure when an exception occurs" in {
    val program = for {
      a <- Task(1)
      b <- Task(a / 0)
    } yield b

    an[FiberFailure] should be thrownBy unsafeRun(program)
  }

  /* probes the function 'ZIO.either' */
  it should "should return a Left containing an exception" in {
    val program = for {
      a <- Task(1)
      b <- Task(a / 0)
    } yield b

    val result = unsafeRun(program.either)
    result shouldBe Symbol("left")
    result.swap.map(_ shouldBe an[ArithmeticException])
  }

  it should "refine a failure" in {
    import com.cmartin.utils.ZioLearn.refineError

    val program: Task[Int] = for {
      a <- Task.effect(1 + 1)
      b <- Task.effect(a / 0)
    } yield b

    val programRefined: IO[MyDomainException, Int] = program.refineOrDie(refineError())

    val result = unsafeRun(programRefined.either)

    result.swap.map(_ shouldBe a[MyExceptionTwo])
  }

  it should "map an error from None to String" in {
    val none: Option[Int] = None
    val noneZio: IO[Unit, Int] = ZIO.fromOption(none)
    val program: IO[String, Int] = noneZio.mapError(_ => "mapped error")

    a[FiberFailure] should be thrownBy unsafeRun(program)
  }

  it should "return a Left with an string error" in {
    val none: Option[Int] = None
    val noneZio: IO[Unit, Int] = ZIO.fromOption(none)
    val program: IO[String, Int] = noneZio.mapError(_ => "mapped error")

    val result: Either[String, Int] = unsafeRun(program.either)

    result shouldBe Left("mapped error")
  }

  it should "exclude a sequence of groups" in {
    import ZioLearnSpec._

    val exclusionList = List("group-2", "group-4")
    val result = artifacts.filterNot(dep => exclusionList.contains(dep.group))

    result shouldBe filteredArtifacts
  }
}

object ZioLearnSpec {
  val artifacts: List[Gav] = List(
    Gav("group-1", "a11", "v11"),
    Gav("group-1", "a12", "v12"),
    Gav("group-2", "a21", "v21"),
    Gav("group-3", "a31", "v31"),
    Gav("group-4", "a4", "v41")
  )

  val filteredArtifacts: List[Gav] = List(
    Gav("group-1", "a11", "v11"),
    Gav("group-1", "a12", "v12"),
    Gav("group-3", "a31", "v31")
  )
}
