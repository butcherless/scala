package com.cmartin.utils.file

import com.cmartin.utils.http.HttpManager
import com.cmartin.utils.http.ZioHttpManager
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
      ZLayer.scoped(HttpClientZioBackend.scoped()),
      ZioHttpManager.layer
    )

  it should "retrieve a single dependency change" in {
    // given
    val deps = Seq(zioDep)

    // when
    val program = HttpManager.checkDependencies(deps)
    val results = runtime.unsafeRun(
      program.provide(applicationLayer)
    )

    info(s"errors: ${results.errors}")
    info(s"(local,remote): ${results.gavList}")

    // then
    results.errors shouldBe empty
    results.gavList should have size 1
    val pair = results.gavList.head
    pair.local.group shouldBe pair.remote.group
    pair.local.artifact shouldBe pair.remote.artifact
    // TODO assert: remote > local
    takeMajorNumber(pair.local.version) shouldBe takeMajorNumber(pair.remote.version)
  }

  it should "retrieve multiple dependency changes" in {
    // given
    val deps    = Seq(zioDep, logbackDep)
    // when
    val program = HttpManager.checkDependencies(deps)

    val results = runtime.unsafeRun(
      program.provide(applicationLayer)
    )

    info(s"errors: ${results.errors}")
    info(s"(local,remote): ${results.gavList}")

    // then
    results.errors shouldBe empty
    results.gavList should have size 2
  }

  it should "retrieve a list of failures" in {
    // given
    val dep     = zioDep.copy(artifact = "missing-zio")
    val deps    = Seq(dep)
    // when
    val program = HttpManager.checkDependencies(deps)

    val results = runtime.unsafeRun(
      program.provide(applicationLayer)
    )

    info(s"errors: ${results.errors}")
    info(s"(local,remote): ${results.gavList}")

    // then
    results.gavList shouldBe empty
    results.errors should have size 1
    val failure = results.errors.head
    failure shouldBe ResponseError(s"no remote dependency found for: $dep")
  }

}
