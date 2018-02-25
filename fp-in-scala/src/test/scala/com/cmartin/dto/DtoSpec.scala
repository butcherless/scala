package com.cmartin.dto

import org.specs2.mutable.Specification

class ServicesSpec extends Specification {
  val ID = 1234
  val REGISTRATION = "EC-LXM"
  val MODEL = "350-800"

  "mandatory constructor arguments" >> {
    val plane = Plane(ID, REGISTRATION)
    val t1 = (plane.id, plane.registration)
    val resTuple = (ID, REGISTRATION)
    resTuple must beEqualTo(t1)
    plane.brand.isEmpty must beTrue
    plane.model.isEmpty must beTrue
  }

  "named constructor arguments" >> {
    val plane = Plane(ID, REGISTRATION, model = MODEL)
    val t1 = (plane.id, plane.registration, plane.model)
    val resTuple = (ID, REGISTRATION, MODEL)
    resTuple must beEqualTo(t1)
  }

}