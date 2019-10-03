package com.cmartin.utils

import com.cmartin.utils.JsonManager.Action
import org.scalatest.{FlatSpec, Matchers}
import zio.{DefaultRuntime, UIO, ZIO}

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

  it should "fail trying to extract an action from the json message" in {
    val message = ""

    val effectResult: UIO[Either[JsonError, Action]] = simulateJsonMapping(message)

    val error: Either[JsonError, Action] = runtime.unsafeRun(effectResult)

    error.swap.contains(MappingError("invalid type")) shouldBe true
  }

  it should "process actions in parallel" in {
    val artifactList :  List[Gav] = List(
      Gav("g1","a11","v1"),
      Gav("g1","a12","v2"),
      Gav("g1","a13","v5"),
      Gav("g2","a21","v32"),
      Gav("g2","a22","v31"),
      Gav("g3","a31","v23"),
      Gav("g3","a32","v23"),
      Gav("g3","a33","v23"),
      Gav("g3","a34","v23"),
      Gav("g3","a35","v23"),
      Gav("g3","a36","v43"),
      Gav("g4","a41","v41")
    )

    val time0 = System.currentTimeMillis()
    val result = ZIO.foreachParN(4)(artifactList)(checkDependency)
    runtime.unsafeRun(result)
    val time1 = System.currentTimeMillis()
    val timeElapsed = (time1 - time0) / 1000.toDouble
    println(s"processing time was: $timeElapsed")
    //Thread.sleep(5000)

  }

}
