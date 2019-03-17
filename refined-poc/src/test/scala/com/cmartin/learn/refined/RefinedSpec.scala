package com.cmartin.learn.refined

import eu.timepit.refined.auto._
import org.scalatest.EitherValues._
import org.scalatest._

import scala.util.Right

class RefinedSpec extends FlatSpec with Matchers {

  it should "validate a positive integer" in {
    val a: Int = 1
    val res = validatePositiveInt(a)

    res map (_.toInt) shouldBe (Right(a))
  }

  it should "reject a negative integer" in {
    val a: Int = -1
    val res = validatePositiveInt(a)

    res.left.value.contains(Constants.PredicateFailed) shouldBe true
  }

  it should "validate an even positive integer" in {
    val a: Int = 16
    val res = validateEvenPositive(a)

    res map (_.toInt) shouldBe (Right(a))
  }

  it should "reject an odd positive integer" in {
    val a: Int = 17
    val res = validateEvenPositive(a)

    res.left.value.contains(Constants.PredicateFailed) shouldBe true
  }
}

object Constants {
  val PredicateFailed = "failed"
}