package com.cmartin

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.{And, Or}
import eu.timepit.refined.numeric._


package object learn {

  trait Greeting {
    lazy val greeting: String = "simple-application-hello"
  }

  type EvenPositive = Positive And Even // TODO
  type WellKnownPort = Interval.Closed[W.`0`.T, W.`1023`.T]
  type UserPort = Interval.Closed[W.`1024`.T, W.`65535`.T]
  type NetworkPort = WellKnownPort Or UserPort

  def validatePositiveInt(a: Int): Either[String, Refined[Int, Positive]] = refineV(a)

  def validateEvenPositive(a: Int): Either[String, Refined[Int, EvenPositive]] = refineV(a)

  def validateWellKnownPort(a: Int): Either[String, Refined[Int, WellKnownPort]] = refineV(a)

  def validateUserPort(a: Int): Either[String, Refined[Int, UserPort]] = refineV(a)

  def validateNetworkPort(a: Int): Either[String, Refined[Int, NetworkPort]] = refineV(a)

}
