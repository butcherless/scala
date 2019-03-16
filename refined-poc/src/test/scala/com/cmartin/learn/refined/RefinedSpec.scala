package com.cmartin.learn.refined

import org.scalatest.EitherValues._
import org.scalatest._

class RefinedSpec extends FlatSpec with Matchers {

  it should "validate a positive integer" in {
    val a: Int = 1
    val res = validatePositiveInt(a)

    res.right.get.value shouldBe a
  }

  it should "reject a negative integer" in {
    val a: Int = -1
    val res = validatePositiveInt(a)

    res.left.value.contains(Constants.PredicateFailed) shouldBe true
  }

  it should "validate an even positive integer" in {
    val a: Int = 16
    val res = validateEvenPositive(a)

    res.right.get.value shouldBe a
  }

  it should "reject an odd positive integer" in {
    val a: Int = 17
    val res = validateEvenPositive(a)

    res.left.value.contains(Constants.PredicateFailed) shouldBe true
  }
}

object Constants {
  val PredicateFailed = "failed"
  val Port22 = 22
  val Port2222 = 2222
  val Port8080 = 8080
  val zipCode28020 = "28020"
}