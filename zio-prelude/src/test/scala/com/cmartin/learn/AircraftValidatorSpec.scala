package com.cmartin.learn

import com.cmartin.learn.Model._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.NonEmptyChunk
import zio.prelude.Validation
import zio.prelude.Validation.Failure

class AircraftValidatorSpec extends AnyFlatSpec with Matchers {

  import AircraftValidator._
  import AircraftValidatorSpec._

  behavior of "Aircraft Validator"

  it should "validate an aircraft" in {
    val result: Validation[ValidationError, Aircraft] = validate(modelOne, registrationOne, countryOne, deliveryOne)

    result shouldBe Validation(aircraftOne)
  }

  it should "fail to validate empty attributes" in {
    val result = validate("", "", "", "")

    result shouldBe Failure(
      NonEmptyChunk(EmptyModelError, EmptyRegistrationError, EmptyCountryError, EmptyDeliveryError)
    )
  }

  it should "fail to validate characters in delivery date text" in {
    val result = validate(modelOne, registrationOne, countryOne, "2013-XY")

    result shouldBe Failure(NonEmptyChunk(InvalidCharactersError))
  }

  it should "fail to validate characters and length in delivery date text" in {
    val result = validate(modelOne, registrationOne, countryOne, "2013-XYZ")

    result shouldBe Failure(NonEmptyChunk(InvalidCharactersError, InvalidLengthError))
  }

  it should "fail to validate a country text code" in {
    val country = "SPaIN34"

    val result = validateLength(country, 2) &> validateLetterChars(country) &> validateUpperCaseChars(country)
    result shouldBe Failure(NonEmptyChunk(InvalidLengthError, LowerCaseLetterError, InvalidCharactersError))
  }
}

object AircraftValidatorSpec {

  val modelOne        = "B738"
  val registrationOne = "EC-LUT"
  val countryOne      = "B738"
  val deliveryOne     = "2013-04"

  val aircraftOne: Aircraft = Aircraft(modelOne, registrationOne, countryOne, deliveryOne)

}
