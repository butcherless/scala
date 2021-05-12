package com.cmartin.learn

import com.cmartin.learn.DummyEntityValidator.validate
import com.cmartin.learn.Model._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.NonEmptyChunk

class DummyEntityValidatorSpec extends AnyFlatSpec with Matchers {

  behavior of "DummyEntity Validator"

  val number = 7
  val text   = "bcdfghjklmnpqrstvwxyz"

  it should "validate a dummy entity" in {
    val result = validate(number, text).toEither

    result shouldBe Right(DummyEntity(number, text))
  }

  it should "fail to validate an empty text" in {
    val result = validate(number, "").toEither

    result shouldBe Left(NonEmptyChunk(EmptyTextError))
  }

  it should "fail to validate an out of range number" in {
    val result = validate(99, text).toEither

    result shouldBe Left(NonEmptyChunk(OutOfRangeError))
  }

  it should "fail to validate an odd number" in {
    val result = validate(2, text).toEither

    result shouldBe Left(NonEmptyChunk(EvenNumberError))
  }

  it should "fail to validate a number out of range and an empty text" in {
    val result = validate(-1, "").toEither

    result shouldBe Left(NonEmptyChunk(OutOfRangeError, EmptyTextError))
  }

  it should "fail to validate a text with invalid characters" in {
    val result = validate(number, "xyza").toEither

    result shouldBe Left(NonEmptyChunk(InvalidCharactersError))
  }

  it should "fail to validate a text with invalid characters and upper chars" in {
    val result = validate(number, "xazY").toEither

    result shouldBe Left(NonEmptyChunk(InvalidCharactersError, UpperCaseLetterError))
  }

  it should "fail to validate a text with invalid characters and upper chars and number out of range" in {
    val result = validate(-1, "xazY").toEither

    result shouldBe Left(NonEmptyChunk(OutOfRangeError, InvalidCharactersError, UpperCaseLetterError))
  }
}
