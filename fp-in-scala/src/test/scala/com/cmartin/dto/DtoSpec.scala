package com.cmartin.dto

import org.specs2.mutable.Specification

class DtoSpec extends Specification {
  val ID = 1234
  val REGISTRATION = "EC-LXM"
  val MODEL = "350-800"
  val EMPTY_STRING = ""

  "mandatory constructor arguments" >> {
    val tuple = plane2Tuple(Plane(ID, REGISTRATION))
    val resTuple = (ID, REGISTRATION, EMPTY_STRING, EMPTY_STRING)
    resTuple must beEqualTo(tuple)
  }

  "named constructor arguments" >> {
    val tuple = plane2Tuple(Plane(ID, REGISTRATION, model = MODEL))
    val resTuple = (ID, REGISTRATION, EMPTY_STRING, MODEL)
    resTuple must beEqualTo(tuple)
  }

  def plane2Tuple(p: Plane) = Plane.unapply(p).get
}