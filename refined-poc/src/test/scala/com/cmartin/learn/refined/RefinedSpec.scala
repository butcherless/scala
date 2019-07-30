package com.cmartin.learn.refined

import org.scalatest._

class RefinedSpec extends FlatSpec with Matchers {

  it should "validate a positive integer" in {
    val a: Int = 1
    val res = validatePositiveInt(a)

    res contains a
  }

  it should "reject a negative integer" in {
    val a: Int = -1
    val res = validatePositiveInt(a)

    res.isLeft shouldBe true
  }

  it should "validate an even positive integer" in {
    val a: Int = 16
    val res = validateEvenPositive(a)

    res contains a
  }

  it should "reject an odd positive integer" in {
    val a: Int = 17
    val res = validateEvenPositive(a)

    res.isLeft shouldBe true
  }
}
