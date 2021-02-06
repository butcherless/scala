package com.cmartin.learn

import com.cmartin.learn.Model._
import zio.prelude.Validation

object DummyEntityValidator {

  private val validRange: Seq[Int] = Range.inclusive(1, 10)

  def validate(number: Int, text: String): Validation[ValidationError, DummyEntity] = {
    Validation
      .mapParN(
        validateNumber(number),
        validateText(text)
      )(DummyEntity)
  }

  private def validateNumber(number: Int): Validation[ValidationError, Int] = {
    for {
      rangeValid <- validateInRange(number)
      result     <- validateOddNumber(rangeValid)
    } yield result
  }

  private def validateInRange(number: Int): Validation[ValidationError, Int] = {
    if (validRange.contains(number)) Validation.succeed(number)
    else Validation.fail(OutOfRangeError)
  }

  private def validateOddNumber(number: Int): Validation[ValidationError, Int] = {
    if (number % 2 == 1) Validation.succeed(number)
    else Validation.fail(EvenNumberError)
  }

  private def validateText(text: String): Validation[ValidationError, String] = {
    validateEmptyText(text) >>> validateCharacters(text) *> validateUpperCase(text)
  }

  private def validateEmptyText(text: String): Validation[ValidationError, String] = {
    if (text.nonEmpty) Validation.succeed(text)
    else Validation.fail(EmptyTextError)
  }

  private def validateCharacters(text: String): Validation[ValidationError, String] = {
    if (text.forall(isConsonantLetter)) Validation.succeed(text)
    else Validation.fail(InvalidCharactersError)
  }

  private def isConsonantLetter(c: Char) =
    c.isLetter && !"aeiouAEIOU".contains(c)

  private def validateUpperCase(text: String): Validation[ValidationError, String] = {
    if (text.forall(_.isLower)) Validation.succeed(text)
    else Validation.fail(UpperCaseLetterError)
  }
}
