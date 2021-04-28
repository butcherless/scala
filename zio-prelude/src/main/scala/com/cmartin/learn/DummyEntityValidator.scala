package com.cmartin.learn

import com.cmartin.learn.Model._
import zio.prelude.Validation
import zio.prelude.ZValidation

object DummyEntityValidator {

  private val validRange: Seq[Int] = Range.inclusive(1, 10)

  def validate(number: Int, text: String): Validation[ValidationError, DummyEntity] = {
    ZValidation.validateWith(
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
    ZValidation
      .fromPredicateWith[ValidationError, Int](OutOfRangeError)(number)(validRange.contains(_))
  }

  private def validateOddNumber(number: Int): Validation[ValidationError, Int] = {
    ZValidation
      .fromPredicateWith[ValidationError, Int](EvenNumberError)(number)(_ % 2 == 1)
  }

  private def validateText(text: String): Validation[ValidationError, String] = {
    validateEmptyText(text) *>
      ZValidation.validateWith(
        validateCharacters(text),
        validateUpperCase(text)
      )((_, _) => text)
  }

  private def validateEmptyText(text: String): Validation[ValidationError, String] = {
    ZValidation
      .fromPredicateWith[ValidationError, String](EmptyTextError)(text)(_.nonEmpty)
  }

  private def validateCharacters(text: String): Validation[ValidationError, String] = {
    ZValidation
      .fromPredicateWith[ValidationError, String](InvalidCharactersError)(text)(_.forall(isConsonantLetter))
  }

  private def isConsonantLetter(c: Char) =
    c.isLetter && !"aeiouAEIOU".contains(c)

  private def validateUpperCase(text: String): Validation[ValidationError, String] = {
    ZValidation
      .fromPredicateWith[ValidationError, String](UpperCaseLetterError)(text)(_.forall(_.isLower))
  }
}
