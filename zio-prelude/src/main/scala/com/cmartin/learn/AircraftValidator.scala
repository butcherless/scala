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
    * @param model
    *   aircraft type, p.e.: airbus A320, boeing 757-200, etc
    * @param registration
    *   aircraft identifier
    * @param country
    *   country to which the aircraft belongs
    * @param delivery
    *   years since delivery
    * @return
    *   an Aircraft or a non empty list of errors
    */
  def validate(
      model: String,
      registration: String,
      country: String,
      delivery: String
  ): Validation[ValidationError, Aircraft] = {
    Validation.validateWith(
      validateModel(model),
      validateRegistration(registration),
      validateCountry(country),
      validateDelivery(delivery)
    )(Aircraft)
  }

  def validateModel(model: String): Validation[ValidationError, String] = {
    validateEmptyText(model, ValidationError.EmptyModelError)
  }

  def validateRegistration(
      registration: String
  ): Validation[ValidationError, String] = {
    validateEmptyText(registration, ValidationError.EmptyRegistrationError)
  }

  def validateCountry(country: String): Validation[ValidationError, String] = {
    validateEmptyText(country, ValidationError.EmptyCountryError)
  }

  /* 1. validate empty text first (AND) (1 error), stop validation if fails
     2. validateChars (OR) length, 1 or 2 errors if fails (NEL)
   */
  def validateDelivery(
      delivery: String
  ): Validation[ValidationError, String] = {
    validateEmptyText(delivery, ValidationError.EmptyDeliveryError)
      .flatMap(_ =>
        Validation
          .validate(
            validateDeliveryChars(delivery),
            validateDeliveryLength(delivery)
          )
          .map(_ => delivery)
      )
  }

  def validateDeliveryChars(
      delivery: String
  ): Validation[ValidationError, String] = {
    val validChars = "0123456789-"
    Validation
      .fromPredicateWith(ValidationError.InvalidCharactersError)(delivery)(
        _.forall(validChars.contains(_))
      )
  }

  def validateUpperCaseChars(
      text: String
  ): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith(ValidationError.LowerCaseLetterError)(text)(_.forall(_.isUpper))
  }

  def validateLetterChars(text: String): Validation[ValidationError, String] = {
    if (text.forall(_.isUpper)) Validation.succeed(text)
    Validation.fail(ValidationError.InvalidCharactersError)
  }

  def validateDeliveryLength(
      delivery: String
  ): Validation[ValidationError, String] = {
    // TODO regex?
    def validateDate(dateText: String) = {
      val x1: Array[String] = delivery.split('-')
      (x1.length == 2) && (x1(0).length == 4 && x1(1).length == 2)
    }

    Validation
      .fromPredicateWith(ValidationError.InvalidLengthError)(delivery)(validateDate)
  }

  def validateLength(
      text: String,
      length: Int
  ): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith(ValidationError.InvalidLengthError)(text)(_.length == length)
  }

  private def validateEmptyText(
      text: String,
      error: ValidationError
  ): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith(error)(text)(_.nonEmpty)
  }
}
