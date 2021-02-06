package com.cmartin.learn

import com.cmartin.learn.Model._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Chunk
import zio.prelude.Validation

class AircraftValidatorSpec extends AnyFlatSpec with Matchers {

  import AircraftValidator._
  import AircraftValidatorSpec._

  behavior of "Aircraft Validator"

  it should "validate an aircraft" in {
    val validation: Validation[ValidationError, Aircraft] =
      AircraftValidator.validate(modelOne, registrationOne, countryOne, deliveryOne)

    val result = validation.sandbox.either.run
    result shouldBe Right(aircraftOne)
  }

  it should "fail to validate empty attributes" in {
    val validation = AircraftValidator.validate("", "", "", "")

    val result = validation.sandbox.either.run
    result.isLeft shouldBe true
    result.swap.map { cause =>
      cause.toChunk shouldBe Chunk(
        EmptyModelError,
        EmptyRegistrationError,
        EmptyCountryError,
        EmptyDeliveryError
      )
    }
  }

  it should "fail to validate characters in delivery date text" in {
    val validation = AircraftValidator.validate(modelOne, registrationOne, countryOne, "2013-XY")
    val result     = validation.sandbox.either.run

    result.isLeft shouldBe true
    result.swap.map { cause =>
      cause.toChunk shouldBe Chunk(InvalidCharactersError)
    }
  }

  it should "TODO fail to validate characters and length in delivery date text" in {
    val validation = validate(modelOne, registrationOne, countryOne, "2013-XYZ")

    val result = validation.sandbox.either.run

    result.isLeft shouldBe true
    result.swap.map { cause =>
      cause.toChunk shouldBe Chunk(InvalidCharactersError, InvalidLengthError)
    }
  }

  it should "fail to validate a country text code" in {
    val country = "SPaIN34"

    val validation =
      Validation.mapParN(
        validateLength(country, 2),
        validateLetterChars(country),
        validateUpperCaseChars(country)
      )((_, _, _) => country)

    val result = validation.sandbox.either.run

    result.isLeft shouldBe true
    result.swap.map { cause =>
      cause.toChunk shouldBe Chunk(InvalidLengthError, InvalidCharactersError, LowerCaseLetterError)
    }
  }

}

object AircraftValidatorSpec {

  val modelOne        = "B738"
  val registrationOne = "EC-LUT"
  val countryOne      = "B738"
  val deliveryOne     = "2013-04"

  val aircraftOne: Aircraft = Aircraft(modelOne, registrationOne, countryOne, deliveryOne)

}
