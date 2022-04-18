package com.cmartin.zio

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.config.ConfigHelper._
import com.cmartin.utils.model.Domain.Gav
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio._
import zio.config._
import zio.config.typesafe._

class ZioLearnSpec
    extends AnyFlatSpec
    with Matchers {

  val runtime = Runtime.default

  it should "map None to a String into the error channel" in {
    val none: Option[Int]                 = None
    val noneZio: IO[Option[Nothing], Int] = ZIO.fromOption(none)
    val program: IO[String, Int]          = noneZio.orElseFail("mapped error")

    a[FiberFailure] should be thrownBy runtime.unsafeRun(program)
  }

  it should "return a Left with an string error" in {
    val none: Option[Int]                 = None
    val noneZio: IO[Option[Nothing], Int] = ZIO.fromOption(none)
    val program: IO[String, Int]          = noneZio.mapError(_ => "mapped error")

    val result: Either[String, Int] = runtime.unsafeRun(program.either)

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
        _      <- Task.attempt(1 / 1).orElseFail(ErrorOne("error-one"))
        result <- Task.attempt(1 / 0).orElseFail(ErrorTwo("error-two"))
      } yield result

    val failure = the[FiberFailure] thrownBy runtime.unsafeRun(program)

    failure.cause.failureOption.map { ex => ex shouldBe ErrorTwo("error-two") }
  }

  it should "return a domain error inside Left (either)" in {
    trait DomainError
    case class ErrorOne(m: String) extends DomainError
    case class ErrorTwo(m: String) extends DomainError

    val program: ZIO[Any, DomainError, Int] =
      for {
        _      <- Task.attempt(1 / 0).orElseFail(ErrorOne("error-one"))
        result <- Task.attempt(1 / 0).orElseFail(ErrorTwo("error-two"))
      } yield result

    val result = runtime.unsafeRun(program.either)

    result shouldBe Left(ErrorOne("error-one"))
  }

  "Zio Config" should "read a HOCON configuration string" in {
    val filename       = "/tmp/deps-log.txt"
    val exclusions     = List("exclusion-one", "exclusion-two")
    val expectedConfig = AppConfig(filename, exclusions)
    val hoconConfig    =
      """
        | filename: /tmp/deps-log.txt
        | exclusions: [exclusion-one, exclusion-two]
        |""".stripMargin

    val descriptor: ConfigDescriptor[AppConfig] = ConfigHelper.configDescriptor
    val source: ConfigSource                    = ConfigSource.fromHoconString(hoconConfig)
    val program                                 = read(descriptor.from(source))
    val result                                  = runtime.unsafeRun(program)

    result shouldBe expectedConfig

    // TODO move to integration test dir
    val file        = new java.io.File("dep-analyzer/src/test/resources/application-config.hocon")
    val descriptor2 = ConfigHelper.configDescriptor
    val source2     = TypesafeConfigSource.fromHoconFile(file)
    val program2    = read(descriptor2.from(source2))
    val result2     = runtime.unsafeRun(program2)

    result2 shouldBe expectedConfig

    // TODO move to integration test dir
    val x1       = ZConfig.fromHoconFile(file, descriptor)
    val program3 = getConfig[AppConfig].provideLayer(x1)
    val result3  = runtime.unsafeRun(program3)
    result3 shouldBe expectedConfig
  }

  it should "fail when trying to retrieve a missing property" in {
    val configMap: Map[String, String]                   = Map.empty
    val configLayer: Layer[ReadError[String], AppConfig] =
      ZConfig.fromMap(configMap, ConfigHelper.configDescriptor)

    val program = getConfig[AppConfig]

    val resultEither = runtime.unsafeRun(
      program.provideLayer(configLayer).either
    )

    info(s"$resultEither")

    resultEither.isLeft shouldBe true
    resultEither.left.map { error =>
      error shouldBe a[ReadError.ZipErrors[_]]
      error.size shouldBe 2
    }
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
