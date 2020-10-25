package com.cmartin.learn

import com.cmartin.learn.Model._
import zio.prelude.Validation

/*
    Note:
    Validators for demo purpose only
    Use regex for production code
 */
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

  def validateDelivery(delivery: String): Validation[ValidationError, String] = for {
    nonEmpty <- validateEmptyText(delivery, EmptyDeliveryError)
    //TODO missing char '-'
    result <- validateDeliveryChars(nonEmpty) &> validateDeliveryLength(nonEmpty)
  } yield result

  def validateDeliveryChars(delivery: String): Validation[ValidationError, String] = {
    val validChars = "0123456789-"
    Validation
      .fromPredicateWith(InvalidCharactersError, delivery)(_.forall(c => validChars.contains(c)))
  }

  def validateDeliveryLength(delivery: String): Validation[ValidationError, String] = {
    val x1: Array[String] = delivery.split('-')
    val result            = (x1.length == 2) && (x1(0).length == 4 && x1(1).length == 2)
    Validation.fromPredicateWith(InvalidLengthError, delivery)(_ => result)
  }

  private def validateEmptyText(text: String, error: ValidationError) = {
    Validation.fromPredicateWith(error, text)(_.nonEmpty)
  }
}
