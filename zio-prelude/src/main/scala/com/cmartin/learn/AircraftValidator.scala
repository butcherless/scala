package com.cmartin.learn

import com.cmartin.learn.Model._
import zio.prelude.Validation

object AircraftValidator {

  /** Validates an Aircraft entity.
    * @param model aircraft type, p.e.: airbus A320, boeing 757-200, etc
    * @param registration aircraft identifier
    * @param country country to which the aircraft belongs
    * @param delivery years since delivery
    * @return an Aircraft or a non empty list of errors
    */
  def validate(
      model: String,
      registration: String,
      country: String,
      delivery: String
  ): Validation[ValidationError, Aircraft] = {
    Validation.mapParN(
      validateModel(model),
      validateRegistration(registration),
      validateCountry(country),
      validateDelivery(delivery)
    )(Aircraft)
  }

  def validateModel(model: String): Validation[ValidationError, String] = {
    validateEmptyText(model, EmptyModelError)
  }

  def validateRegistration(registration: String): Validation[ValidationError, String] = {
    validateEmptyText(registration, EmptyRegistrationError)
  }

  def validateCountry(country: String): Validation[ValidationError, String] = {
    validateEmptyText(country, EmptyCountryError)
  }

  def validateDelivery(delivery: String): Validation[ValidationError, String] = {
    validateEmptyText(delivery, EmptyDeliveryError)
  }

  private def validateEmptyText(text: String, error: ValidationError) = {
    if (text.isEmpty) Validation.fail(error)
    else Validation.succeed(text)

  }
}
