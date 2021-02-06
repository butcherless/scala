package com.cmartin.learn

import com.cmartin.learn.DummyEntityValidator.validate
import com.cmartin.learn.Model._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DummyEntityValidatorSpec extends AnyFlatSpec with Matchers {

  import Commons._

  behavior of "DummyEntity Validator"

  val number = 7
  val text   = "bcdfghjklmnpqrstvwxyz"

  it should "validate a dummy entity" in {
    val result = validate(number, text).sandbox.either.run

    result shouldBe Right(DummyEntity(number, text))
  }

  it should "fail to validate an empty text" in {
    val validation = validate(number, "").sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(EmptyTextError))
  }

  it should "fail to validate an out of range number" in {
    val validation = validate(99, text).sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(OutOfRangeError))
  }

  it should "fail to validate an odd number" in {
    val validation = validate(2, text).sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(EvenNumberError))
  }

  it should "fail to validate a number out of range and a text empty" in {
    val validation = validate(-1, "").sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(OutOfRangeError, EmptyTextError))
  }

  it should "fail to validate a text with invalid characters" in {
    val validation = validate(number, "xyza").sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(InvalidCharactersError))
  }

  it should "fail to validate a text with invalid characters and upper chars" in {
    val validation = validate(number, "xazY").sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(InvalidCharactersError, UpperCaseLetterError))
  }

  it should "fail to validate a text with invalid characters and upper chars and number out of range" in {
    val validation = validate(-1, "xazY").sandbox.either.run
    val result     = causeToErrorList(validation)

    result shouldBe Left(List(OutOfRangeError, InvalidCharactersError, UpperCaseLetterError))
  }

}
