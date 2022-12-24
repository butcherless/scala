package com.cmartin.learn

import com.cmartin.learn.Model._
import com.cmartin.learn.Model.ValidationError._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.NonEmptyChunk
import zio.prelude.Validation

class AircraftValidatorSpec extends AnyFlatSpec with Matchers {

  import AircraftValidator._
  import AircraftValidatorSpec._

  behavior of "Aircraft Validator"

  it should "validate an aircraft" in {
    val result =
      AircraftValidator
        .validate(modelOne, registrationOne, countryOne, deliveryOne)
        .toEither

    result shouldBe Right(aircraftOne)
  }

  it should "fail to validate empty attributes" in {
    val result =
      AircraftValidator
        .validate("", "", "", "")
        .toEither

    result shouldBe Left(
      NonEmptyChunk(
        EmptyModelError,
        EmptyRegistrationError,
        EmptyCountryError,
        EmptyDeliveryError
      )
    )
  }

  it should "fail to validate characters in delivery date text" in {
    val result =
      AircraftValidator
        .validate(modelOne, registrationOne, countryOne, "2013-XY")
        .toEither

    result shouldBe Left(NonEmptyChunk(InvalidCharactersError))
  }

  it should "fail to validate characters and length in delivery date text" in {
    val result =
      validate(modelOne, registrationOne, countryOne, "2013-XYZ").toEither

    result shouldBe Left(
      NonEmptyChunk(InvalidCharactersError, InvalidLengthError)
    )
  }

  it should "fail to validate a country text code" in {
    val country = "SPaIN34"

    val result =
      Validation
        .validateWith(
          validateLength(country, 2),
          validateLetterChars(country),
          validateUpperCaseChars(country)
        )((_, _, _) => country)
        .toEither

    result shouldBe Left(
      NonEmptyChunk(
        InvalidLengthError,
        InvalidCharactersError,
        LowerCaseLetterError
      )
    )
  }

  "Delivery" should "fail to validate an empty delivery date text" in {
    AircraftValidator.validateDelivery("").toEither shouldBe
      Left(NonEmptyChunk(EmptyDeliveryError))
  }
}

object AircraftValidatorSpec {

  val modelOne        = "B738"
  val registrationOne = "EC-LUT"
  val countryOne      = "B738"
  val deliveryOne     = "2013-04"

  val aircraftOne: Aircraft =
    Aircraft(modelOne, registrationOne, countryOne, deliveryOne)

}
