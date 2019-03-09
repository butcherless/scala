package com.cmartin.learn

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalatest.EitherValues._
import org.scalatest._

class SimpleAppSpec extends FlatSpec with Matchers {
  "The SimpleApp object" should "say hello" in {
    SimpleApp.greeting shouldEqual "simple-application-hello"
  }

  it should "validate a positive integer" in {
    val expected: Int Refined Positive = 1
    val a: Int = 1
    val res = validatePositiveInt(a)

    res.right.value shouldBe expected
  }

  it should "reject a negative integer" in {
    val a: Int = -1
    val res = validatePositiveInt(a)

    res.left.value.contains(Constants.PredicateFailed) shouldBe true
  }

  it should "validate an even positive integer" in {
    val expected: Int Refined Positive = 16
    val a: Int = 16
    val res = validateEvenPositive(a)

    res.right.value shouldBe expected
  }

  it should "reject an odd positive integer" in {
    val a: Int = 17
    val res = validateEvenPositive(a)

    res.left.value.contains(Constants.PredicateFailed) shouldBe true
  }

  it should "validate a well known port" in {
    val expected: Int Refined WellKnownPort = 22
    val a: Int = 22
    val res = validateWellKnownPort(a)

    res.right.get shouldBe expected
  }

  it should "reject a non well known port" in {
    val expected = None
    val a: Int = 2222
    val res = validateWellKnownPort(a)

    res.toOption shouldBe expected
  }

  it should "validate an user port" in {
    val expected: Int Refined UserPort = 8080
    val a: Int = 8080
    val res = validateUserPort(a)

    res.right.get shouldBe expected
  }

  it should "reject a non user port" in {
    val expected = None
    val a: Int = 22
    val res = validateUserPort(a)

    res.toOption shouldBe expected
  }

  it should "validate a network port" in {
    val port1 = 22
    val port2 = 2222
    val nport1: Int Refined NetworkPort = 22
    val nport2: Int Refined NetworkPort = 2222

    val res1 = validateNetworkPort(port1)
    val res2 = validateNetworkPort(port2)

    res1.right.get shouldBe nport1
    res2.right.get shouldBe nport2
  }

}

object Constants {
  val PredicateFailed = "failed"
}