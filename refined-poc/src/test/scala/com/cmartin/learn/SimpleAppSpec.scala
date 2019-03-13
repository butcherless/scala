package com.cmartin.learn

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.string.ValidInt
import org.scalatest.EitherValues._
import org.scalatest._

class SimpleAppSpec extends FlatSpec with Matchers {

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
    val a: Int = Constants.Port22
    val res = validateWellKnownPort(a)

    res.right.get shouldBe expected
  }

  it should "reject a non well known port" in {
    val expected = None
    val a: Int = Constants.Port2222
    val res = validateWellKnownPort(a)

    res.toOption shouldBe expected
  }

  it should "validate an user port" in {
    val expected: Int Refined UserPort = 8080
    val a: Int = Constants.Port8080
    val res = validateUserPort(a)

    res.right.get shouldBe expected
  }

  it should "reject a non user port" in {
    val expected = None
    val a: Int = Constants.Port22
    val res = validateUserPort(a)

    res.toOption shouldBe expected
  }

  it should "validate a network port" in {
    val port1 = Constants.Port22
    val port2 = Constants.Port2222
    val nport1: Int Refined NetworkPort = 22
    val nport2: Int Refined NetworkPort = 2222

    val res1 = validateNetworkPort(port1)
    val res2 = validateNetworkPort(port2)

    res1.right.get shouldBe nport1
    res2.right.get shouldBe nport2
  }

  it should "validate a zip code string" in {
    val expected: String Refined ValidInt = "28020"
    val zipCode = Constants.zipCode28020

    val res = validateZipCode(zipCode)

    res.right.get shouldBe expected
  }

  it should "reject a low zip code string" in {
    // preconditions
    val expected = None
    val zipCode = "00999"

    // functionality
    val res = validateZipCode(zipCode)

    // verifications
    res.toOption shouldBe expected
  }

  it should "reject an invalid zip code string" in {
    // preconditions
    val expected = None
    val zipCode = "2802K"

    // functionality
    val res = validateZipCode(zipCode)

    // verifications
    res.toOption shouldBe expected
  }


}

object Constants {
  val PredicateFailed = "failed"
  val Port22 = 22
  val Port2222 = 2222
  val Port8080 = 8080
  val zipCode28020 = "28020"
}