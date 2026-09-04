package com.cmartin.learn

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.{And, Not, Or}
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.{MatchesRegex, ValidInt}

package object refined {

  type EvenPositive  = Positive And Even // TODO
  type WellKnownPort = Interval.Closed[0, 1023]
  type UserPort      = Interval.Closed[1024, 65535]
  type NetworkPort   = WellKnownPort Or UserPort
  type ZipCode       = Interval.Closed[1000, 52999]
  type LeapYear      =
    Positive And Divisible[4] And Not[Divisible[100]] Or
      Divisible[400]
  type PersonName    = MatchesRegex["[a-zA-Z][a-zA-Z -]*"]

  def validatePositiveInt(a: Int): Either[String, Refined[Int, Positive]] =
    refineV(a)

  def validateEvenPositive(a: Int): Either[String, Refined[Int, EvenPositive]] =
    refineV(a)

  def validateWellKnownPort(
      a: Int
  ): Either[String, Refined[Int, WellKnownPort]] = refineV(a)

  def validateUserPort(a: Int): Either[String, Refined[Int, UserPort]] =
    refineV(a)

  def validateNetworkPort(a: Int): Either[String, Refined[Int, NetworkPort]] =
    refineV(a)

  def validateZipCode(
      zcs: String
  ): Either[String, Refined[String, ValidInt]] = {
    val validInt: Either[String, Refined[String, ValidInt]] = refineV(zcs)

    validInt match {
      case Right(_)    => {
        refineV[ZipCode](zcs.toInt) match {
          case Right(_)    => validInt
          case Left(value) => Left(value)
        }
      }
      case Left(value) => Left(value)
    }
  }

  def validateLeapYear(y: Int): Either[String, Refined[Int, LeapYear]] =
    refineV(y)

  def validatePersonName(
      name: String
  ): Either[String, Refined[String, PersonName]] = refineV(name)

}
