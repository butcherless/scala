package com.cmartin.utils

import com.cmartin.utils.JsonManager.Action
import org.scalatest.{FlatSpec, Matchers}
import zio.{DefaultRuntime, UIO}

class ZioWarmUpSpec
  extends FlatSpec
    with Matchers {
  //with EitherValues{

  import ZioWarmUp._

  val runtime = new DefaultRuntime {}

  "ZIO UIO effect" should "return a value" in {
    // given
    val a: Int = 2
    val b: Int = 2

    // when
    val program: UIO[Int] = sum(a, b)

    val result: Int = runtime.unsafeRun(program)

    // then
    assert(result == 4)
  }

  "ZIO Task effect" should "return a valid Json" in {
    // given
    val message = "{...non empty message simulation...}"
    //    val message = ""

    // when
    val effectResult: UIO[Either[Throwable, Json]] = simulateParseJson(message)

    // then
    val jsonEither: Either[Throwable, Json] = runtime.unsafeRun(effectResult)

    jsonEither.contains(Json(jsonKeys)) shouldBe true
  }


  it should "return a runtime exception" in {
    // given
    val message = ""

    // when
    val effectResult: UIO[Either[Throwable, Json]] = simulateParseJson(message)

    // then
    val jsonEither = runtime.unsafeRun(effectResult)

    //val exception = jsonEither.fold(e => e, v => v)
    val isRuntimeException = jsonEither.swap.exists(_.isInstanceOf[RuntimeException])

    isRuntimeException shouldBe true
  }

  it should "extract an action from the json message" in {
    val message = "{...valid action simulation...}"

    val effectResult: UIO[Either[JsonError, Action]] = simulateJsonMapping(message)

    val action: Either[JsonError, Action] = runtime.unsafeRun(effectResult)

    action.contains(Action(message, 0)) shouldBe true
  }

  it should "ONLY fail trying to extract an action from the json message" in {
    val message = ""

    val effectResult: UIO[Either[JsonError, Action]] = simulateJsonMapping(message)

    val error: Either[JsonError, Action] = runtime.unsafeRun(effectResult)

    error.swap.contains(MappingError("invalid type")) shouldBe true
  }

}
