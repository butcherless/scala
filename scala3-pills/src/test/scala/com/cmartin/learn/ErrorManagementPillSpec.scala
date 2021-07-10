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

    val program = taskApi.doPost(
      Map("owner" -> "1", "name" -> "parser", "def" -> "parser-definition")
    )

    val result = runtime.unsafeRun(program)

    info(s"result: $result")

    result shouldBe "Task(parser,parser-definition)"
  }

  it should "fail to create a duplicate Task" in {

    val program = taskApi.doPost(
      Map("owner" -> "1", "name" -> "parser", "def" -> "error-duplicate")
    )

    val result = runtime.unsafeRun(program.either)

    info(s"result: $result")

    result shouldBe Left(AdapterLayer.AdapterError.Conflict("duplicate-task"))
  }

  it should "fail to create an invalid Task" in {

    val program = taskApi.doPost(
      Map("owner" -> "1", "def" -> "error-duplicate")
    )

    val result = runtime.unsafeRun(program.either)

    info(s"result: $result")

    result shouldBe Left(AdapterLayer.AdapterError.BadRequest("missing name"))
  }

}
