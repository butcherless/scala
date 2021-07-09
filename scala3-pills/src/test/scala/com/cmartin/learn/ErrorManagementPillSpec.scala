package com.cmartin.learn

import zio.Runtime

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ErrorManagementPill._

class ErrorManagementPillSpec extends AnyFlatSpec with Matchers {
  behavior of "ErrorManagementPill"

  val runtime = Runtime.default

  val taskService = ServiceLayer.TaskServiceImpl()
  val taskApi = AdapterLayer.TaskApiImpl(taskService)

  "Create" should "create a Task" in {

    val program = taskApi.doPost("1")

    val result = runtime.unsafeRun(program)

    info(s"result: $result")

    result shouldBe "1"
  }

  it should "fail to create a Task" in {

    val program = taskApi.doPost("create-error")

    val result = runtime.unsafeRun(program.either)

    info(s"result: $result")

    result shouldBe Left(AdapterLayer.AdapterError.Conflict(""))
  }

}
