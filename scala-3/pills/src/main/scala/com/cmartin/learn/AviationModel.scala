package com.cmartin.learn

import zio.prelude.Subtype

import java.time.LocalDate

object AviationModel {

  // TODO move to domain
  trait ProgramError {
    val message: String
  }

  sealed trait ServiceError extends ProgramError
  trait RepositoryError extends ProgramError

  case class MissingEntityError(message: String) extends ServiceError
  case class DuplicateEntityError(message: String) extends ServiceError
  case class UnexpectedServiceError(message: String) extends ServiceError

  object CountryCode extends Subtype[String]
  type CountryCode = CountryCode.Type

  object IataCode extends Subtype[String]
  type IataCode = IataCode.Type

  object IcaoCode extends Subtype[String]
  type IcaoCode = IcaoCode.Type

  case class Country(
      code: CountryCode,
      name: String
  )

  case class Airport(
      name: String,
      iataCode: IataCode,
      icaoCode: IcaoCode,
      country: Country
  )

  case class Airline(
      name: String,
      iataCode: IataCode, // TODO AirlineIataCode
      icaoCode: IcaoCode, // TODO AirlineIcaoCode
      foundationDate: LocalDate,
      country: Country
  )
}
