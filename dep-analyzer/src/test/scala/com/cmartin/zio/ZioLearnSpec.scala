package com.cmartin.zio

import com.cmartin.utils.domain.Model.Gav
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio._

class ZioLearnSpec
    extends AnyFlatSpec
    with Matchers {

  val runtime = Runtime.default

  it should "map None to a String into the error channel" in {
    val none: Option[Int]                 = None
    val noneZio: IO[Option[Nothing], Int] = ZIO.fromOption(none)
    val program: IO[String, Int]          = noneZio.orElseFail("mapped error")

    a[FiberFailure] should be thrownBy Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(program).getOrThrowFiberFailure()
    }

  }

  it should "return a Left with an string error" in {
    val none: Option[Int]                 = None
    val noneZio: IO[Option[Nothing], Int] = ZIO.fromOption(none)
    val program: IO[String, Int]          = noneZio.mapError(_ => "mapped error")

    val result: Either[String, Int] = Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(program.either).getOrThrowFiberFailure()
    }

    result shouldBe Left("mapped error")
  }

  it should "exclude a sequence of groups" in {
    import ZioLearnSpec._

    val exclusionList = List("group-2", "group-4")
    val result        = artifacts.filterNot(dep => exclusionList.contains(dep.group))

    result shouldBe filteredArtifacts
  }

  it should "return a domain error inside fiber failure" in {
    trait DomainError
    case class ErrorOne(m: String) extends DomainError
    case class ErrorTwo(m: String) extends DomainError

    val program: ZIO[Any, DomainError, Int] =
      for {
        _      <- ZIO.attempt(1 / 1).orElseFail(ErrorOne("error-one"))
        result <- ZIO.attempt(1 / 0).orElseFail(ErrorTwo("error-two"))
      } yield result

    val failure = the[FiberFailure] thrownBy Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(program).getOrThrowFiberFailure()
    }

    failure.cause.failureOption.map { ex => ex shouldBe ErrorTwo("error-two") }
  }

  it should "return a domain error inside Left (either)" in {
    trait DomainError
    case class ErrorOne(m: String) extends DomainError
    case class ErrorTwo(m: String) extends DomainError

    val program: ZIO[Any, DomainError, Int] =
      for {
        _      <- ZIO.attempt(1 / 0).orElseFail(ErrorOne("error-one"))
        result <- ZIO.attempt(1 / 0).orElseFail(ErrorTwo("error-two"))
      } yield result

    val result = Unsafe.unsafe { implicit u =>
      runtime.unsafe.run(program.either).getOrThrowFiberFailure()
    }

    result shouldBe Left(ErrorOne("error-one"))
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

  val filename   = "dependencies.data"
  val exclusions = List("dep-exclusion-1")

}
