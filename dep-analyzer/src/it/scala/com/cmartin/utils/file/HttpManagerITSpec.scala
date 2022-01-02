package com.cmartin.utils.file

import com.cmartin.utils.Domain.{Gav, ResponseError}
import com.cmartin.utils.http.{HttpManager, HttpManagerLive}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.{default => runtime}

class HttpManagerITSpec
    extends AnyFlatSpec with Matchers {

  import HttpManagerITSpec._

  behavior of "HttpManager"

  it should "retrieve a single dependency change" in {
    // given
    val deps    = Seq(zioDep)
    // when
    val program = HttpManager(_.checkDependencies(deps))

    val (errors, depPairs) = runtime.unsafeRun(
      program.provide(HttpManagerLive.layer)
    )

    info(s"errors: $errors")
    info(s"(local,remote): $depPairs")

    // then
    errors shouldBe empty
    depPairs should have size 1
    val pair = depPairs.head
    pair.local.group shouldBe pair.remote.group
    pair.local.artifact shouldBe pair.remote.artifact
    takeMajorNumber(pair.local.version) shouldBe takeMajorNumber(pair.remote.version)
  }

  it should "retrieve multiple dependency changes" in {
    // given
    val deps    = Seq(zioDep, logbackDep)
    // when
    val program = HttpManager(_.checkDependencies(deps))

    val (errors, depPairs) = runtime.unsafeRun(
      program.provide(HttpManagerLive.layer)
    )

    info(s"errors: $errors")
    info(s"(local,remote): $depPairs")

    // then
    errors shouldBe empty
    depPairs should have size 2
  }

  it should "WIP retrieve a list of failures" in {
    // given
    val dep     = zioDep.copy(artifact = "missing-zio")
    val deps    = Seq(dep)
    // when
    val program = HttpManager(_.checkDependencies(deps))

    val (errors, depPairs) = runtime.unsafeRun(
      program.provide(HttpManagerLive.layer)
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

object HttpManagerITSpec {
  val zioGroup    = "dev.zio"
  val zioArtifact = "zio_2.13"
  val zioVersion  = "2.0.0"
  val zioDep      = Gav(zioGroup, zioArtifact, zioVersion)

  val lbGroup    = "ch.qos.logback"
  val lbArtifact = "logback-classic"
  val lbVersion  = "1.2.5"
  val logbackDep = Gav(lbGroup, lbArtifact, lbVersion)

  def takeMajorNumber(version: String) = version.split('.').head
}
