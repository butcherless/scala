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
    Validation.validateWith(
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

  /* 1. validate empty text first (AND) (1 error), stop validation if fails
     2. validateChars (OR) length, 1 or 2 errors if fails (NEL)
   */
  def validateDelivery(delivery: String): Validation[ValidationError, String] = {
    validateEmptyText(delivery, EmptyDeliveryError) *>
      Validation.validateWith(
        validateDeliveryChars(delivery),
        validateDeliveryLength(delivery)
      )((_, _) => delivery)
  }

  def validateDeliveryChars(delivery: String): Validation[ValidationError, String] = {
    val validChars = "0123456789-"
    if (delivery.forall(c => validChars.contains(c))) Validation.succeed(delivery)
    else Validation.fail(InvalidCharactersError)
  }

  def validateUpperCaseChars(text: String): Validation[ValidationError, String] = {
    if (text.forall(_.isUpper)) Validation.succeed(text)
    Validation.fail(LowerCaseLetterError)
  }

  def validateLetterChars(text: String): Validation[ValidationError, String] = {
    if (text.forall(_.isUpper)) Validation.succeed(text)
    Validation.fail(InvalidCharactersError)
  }

  def validateDeliveryLength(delivery: String): Validation[ValidationError, String] = {
    val x1: Array[String] = delivery.split('-')
    val result            = (x1.length == 2) && (x1(0).length == 4 && x1(1).length == 2)
    if (result) Validation.succeed(delivery)
    else Validation.fail(InvalidLengthError)
  }

  def validateLength(text: String, length: Int): Validation[ValidationError, String] = {
    if (text.length == length) Validation.succeed(text)
    else Validation.fail(InvalidLengthError)
  }

  private def validateEmptyText(text: String, error: ValidationError): Validation[ValidationError, String] = {
    if (text.nonEmpty) Validation.succeed(text)
    else Validation.fail(error)
  }
}
