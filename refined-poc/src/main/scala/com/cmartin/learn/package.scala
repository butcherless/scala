package com.cmartin

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.{And, Or}
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.ValidInt


package object learn {

  type EvenPositive = Positive And Even // TODO
  type WellKnownPort = Interval.Closed[W.`0`.T, W.`1023`.T]
  type UserPort = Interval.Closed[W.`1024`.T, W.`65535`.T]
  type NetworkPort = WellKnownPort Or UserPort
  type ZipCode = Interval.Closed[W.`1000`.T, W.`52999`.T]

  def validatePositiveInt(a: Int): Either[String, Refined[Int, Positive]] = refineV(a)

  def validateEvenPositive(a: Int): Either[String, Refined[Int, EvenPositive]] = refineV(a)

  def validateWellKnownPort(a: Int): Either[String, Refined[Int, WellKnownPort]] = refineV(a)

  def validateUserPort(a: Int): Either[String, Refined[Int, UserPort]] = refineV(a)

  def validateNetworkPort(a: Int): Either[String, Refined[Int, NetworkPort]] = refineV(a)

  def validateZipCode(zcs: String): Either[String, Refined[String, ValidInt]] = {
    val validInt: Either[String, Refined[String, ValidInt]] = refineV(zcs)

    validInt match {
      case Right(_) => {
        refineV[ZipCode](zcs.toInt) match {
          case Right(_) => validInt
          case Left(value) => Left(value)
        }
      }
      case Left(value) => Left(value)
    }
  }
}
