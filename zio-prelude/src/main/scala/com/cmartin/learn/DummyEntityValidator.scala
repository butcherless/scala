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
    Validation
      .fromPredicateWith[ValidationError, Int](OutOfRangeError)(validRange.contains(_))(number)
  }

  private def validateOddNumber(number: Int): Validation[ValidationError, Int] = {
    Validation
      .fromPredicateWith[ValidationError, Int](EvenNumberError)(_ % 2 == 1)(number)
  }

  private def validateText(text: String): Validation[ValidationError, String] = {
    validateEmptyText(text) *>
      Validation.mapParN(
        validateCharacters(text),
        validateUpperCase(text)
      )((_, _) => text)
  }

  private def validateEmptyText(text: String): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith[ValidationError, String](EmptyTextError)(_.nonEmpty)(text)
  }

  private def validateCharacters(text: String): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith[ValidationError, String](InvalidCharactersError)(_.forall(isConsonantLetter))(text)
  }

  private def isConsonantLetter(c: Char) =
    c.isLetter && !"aeiouAEIOU".contains(c)

  private def validateUpperCase(text: String): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith[ValidationError, String](UpperCaseLetterError)(_.forall(_.isLower))(text)
  }
}
