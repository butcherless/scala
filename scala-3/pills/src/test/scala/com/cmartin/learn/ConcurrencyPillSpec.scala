package com.cmartin.learn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ConcurrencyPill._
import zio.Runtime.{default => runtime}
import zio.ZIO

class ConcurrencyPillSpec
    extends AnyFlatSpec
    with Matchers {

  behavior of "ConcurrencyPill"

  it should "return a delay value" in {
    val process = "procOne"
    val program = doProcess(process)(100)

    val result = runtime.unsafeRun(program)
    info(s"result: $result")

    result shouldBe process
  }

  it should "return a tuple of delay values" in {
    val processOne = "procOne"
    val processTwo = "procTwo"
    val program = doProcess(processOne)(100) zip doProcess(processTwo)(200)

    val result = runtime.unsafeRun(program)
    info(s"result: $result")

    result shouldBe (processOne, processTwo)
  }

  it should "return the first conpleted, all succeed" in {
    val processOne = "procOne"
    val processTwo = "procTwo"
    val program = doProcess(processOne)(100) race doProcess(processTwo)(200)

    val result = runtime.unsafeRun(program)
    info(s"result: $result")

    result shouldBe processOne
  }

  it should "return the first completed, some failed" in {
    val processOne = "procOne"
    val processTwo = "procTwo"
    val program = doFailProcess(processOne)(100) race doProcess(processTwo)(200)

    val result = runtime.unsafeRun(program)
    info(s"result: $result")

    result shouldBe processTwo
  }

  it should "fail for all failed" in {
    val process = "procOne"
    val program = doFailProcess(process)(200) race doFailProcess("procTwo")(100)

    val result = runtime.unsafeRun(program.either)
    info(s"result: $result")

    result shouldBe Left(DomainError.ProcessingError(process))
  }

}
