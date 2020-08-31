package com.cmartin.dto

case class Plane(id: Long, registration: String, brand: String = "", model: String = "")

//TODO refactor to Plane constructor
object Plane {
  def buildPlane(id: Long, registration: String): Option[Plane] = {
    if (id < 1) None
    else Option.unless(registration.isEmpty)(Plane(id, registration))
  }
}
