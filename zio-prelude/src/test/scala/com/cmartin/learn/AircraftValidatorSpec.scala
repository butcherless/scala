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

  it should "fail when validate empty attributes" in {
    val result = validate("", "", "", "")

    result shouldBe Failure(
      NonEmptyChunk(EmptyModelError, EmptyRegistrationError, EmptyCountryError, EmptyDeliveryError)
    )
  }

}

object AircraftValidatorSpec {

  val modelOne        = "B738"
  val registrationOne = "EC-LUT"
  val countryOne      = "B738"
  val deliveryOne     = "2013-04"

  val aircraftOne = Aircraft(modelOne, registrationOne, countryOne, deliveryOne)

}
