package com.cmartin.utils.file

import com.cmartin.utils.http.{HttpManager, ZioHttpManager}
import com.cmartin.utils.model.Domain.ResponseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.Runtime.{default => runtime}
import zio.ZLayer

class ZioHttpManagerITSpec
    extends AnyFlatSpec with Matchers {

  import HttpManagerITSpec._

  behavior of "ZioHttpManager"

  val applicationLayer =
    ZLayer.make[HttpManager](
      ZLayer.succeed(HttpClientZioBackend()),
      ZioHttpManager.layer
    )

  it should "retrieve a single dependency change" in {
    // given
    val deps = Seq(zioDep)

    // when
    val program            = HttpManager(_.checkDependencies(deps))
    val (errors, depPairs) = runtime.unsafeRun(
      program.provide(applicationLayer)
    )

    info(s"errors: $errors")
    info(s"(local,remote): $depPairs")

    // then
    errors shouldBe empty
    depPairs should have size 1
    val pair = depPairs.head
    pair.local.group shouldBe pair.remote.group
    pair.local.artifact shouldBe pair.remote.artifact
    // TODO assert: remote > local
    takeMajorNumber(pair.local.version) shouldBe takeMajorNumber(pair.remote.version)
  }

  it should "retrieve multiple dependency changes" in {
    // given
    val deps    = Seq(zioDep, logbackDep)
    // when
    val program = HttpManager(_.checkDependencies(deps))

    val (errors, depPairs) = runtime.unsafeRun(
      program.provide(applicationLayer)
    )

    info(s"errors: $errors")
    info(s"(local,remote): $depPairs")

    // then
    errors shouldBe empty
    depPairs should have size 2
  }

  it should "retrieve a list of failures" in {
    // given
    val dep     = zioDep.copy(artifact = "missing-zio")
    val deps    = Seq(dep)
    // when
    val program = HttpManager(_.checkDependencies(deps))

    val (errors, depPairs) = runtime.unsafeRun(
      program.provide(applicationLayer)
    )

    info(s"errors: $errors")
    info(s"(local,remote): $depPairs")

    // then
    depPairs shouldBe empty
    errors should have size 1
    val failure = errors.head
    failure shouldBe ResponseError(s"no remote dependency found for: $dep")
  }

}
