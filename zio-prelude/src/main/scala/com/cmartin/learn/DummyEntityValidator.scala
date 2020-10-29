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
      .fromPredicateWith(OutOfRangeError, number)(validRange.contains)
  }

  private def validateOddNumber(number: Int): Validation[ValidationError, Int] = {
    Validation
      .fromPredicateWith(EvenNumberError, number)(_ % 2 == 1)
  }

  private def validateText(text: String): Validation[ValidationError, String] = {
    for {
      nonEmpty <- validateEmptyText(text)
      result   <- validateCharacters(nonEmpty) &> validateUpperCase(nonEmpty)
    } yield result
  }

  private def validateEmptyText(text: String): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith(EmptyTextError, text)(_.nonEmpty)
  }

  private def validateCharacters(text: String): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith(InvalidCharactersError, text)(_.forall(isConsonantLetter))
  }

  private def isConsonantLetter(c: Char) =
    c.isLetter && !"aeiouAEIOU".contains(c)

  private def validateUpperCase(text: String): Validation[ValidationError, String] = {
    Validation
      .fromPredicateWith(UpperCaseLetterError, text)(_.forall(_.isLower))
  }
}
