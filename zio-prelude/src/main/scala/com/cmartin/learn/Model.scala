package com.cmartin.learn

object Model {

  final case class Aircraft(
      model: String,
      registration: String,
      country: String,
      delivery: String
  )
  final case class DummyEntity(number: Int, test: String)

  sealed trait ValidationError

  object ValidationError {
    object EmptyModelError        extends ValidationError
    object EmptyRegistrationError extends ValidationError
    object EmptyCountryError      extends ValidationError
    object EmptyDeliveryError     extends ValidationError
    object InvalidAgeValue        extends ValidationError
    object InvalidCharactersError extends ValidationError
    object InvalidLengthError     extends ValidationError
    object LowerCaseLetterError   extends ValidationError
    object UpperCaseLetterError   extends ValidationError
    object InvalidNumberError     extends ValidationError
    object EmptyTextError         extends ValidationError
    object EvenNumberError        extends ValidationError
    object OutOfRangeError        extends ValidationError
  }
}
