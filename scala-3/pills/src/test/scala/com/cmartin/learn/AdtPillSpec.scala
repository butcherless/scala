package com.cmartin.learn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import AdtPill.ResponseError
import AdtPill.ResponseError.*

class AdtPillSpec extends AnyFlatSpec with Matchers {
  behavior of "AdtPill"

  it should "match a BadRequest error" in {
    val message = "missing id"
    val responseError: ResponseError = BadRequest(message)

    val result = responseError match {
      case BadRequest(m) => m
      case NotFound(m)   => m
      case Conflict(m)   => m
    }

    result shouldBe message
  }
}
