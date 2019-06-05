package com.cmartin.dto

import com.cmartin.dto.Plane.buildPlane
import org.specs2.mutable.Specification

import scala.collection.immutable.HashSet

class DtoSpec extends Specification {
  val ID           = 1234
  val REGISTRATION = "EC-LXM"
  val MODEL        = "350-800"
  val EMPTY_STRING = ""

  def plane2Tuple(p: Plane) = Plane.unapply(p).get

  "mandatory constructor arguments" >> {
    val tuple    = plane2Tuple(Plane(ID, REGISTRATION))
    val resTuple = (ID, REGISTRATION, EMPTY_STRING, EMPTY_STRING)
    resTuple must beEqualTo(tuple)
  }

  "named constructor arguments" >> {
    val tuple    = plane2Tuple(Plane(ID, REGISTRATION, model = MODEL))
    val resTuple = (ID, REGISTRATION, EMPTY_STRING, MODEL)
    resTuple must beEqualTo(tuple)
  }

  "should return a valid Plane" >> {
    val res = buildPlane(ID, REGISTRATION)
    res.isDefined must beTrue
  }

  "should return None" >> {
    val res = buildPlane(-1, REGISTRATION)
    res.isDefined must beFalse
  }

  "should return None" >> {
    val res = buildPlane(ID, "")
    res.isDefined must beFalse
  }

  "should be empty collection" >> {
    val res = Parent(ID, "parent description", HashSet())
    res.id must beEqualTo(ID)
    res.desc.isEmpty must beFalse
    res.children.isEmpty must beTrue
  }

  "should be non empty collection" >> {
    val child = Child(ID, "child description")
    val res   = Parent(ID, "parent description", HashSet(child))
    res.id must beEqualTo(ID)
    res.desc.isEmpty must beFalse
    res.children.isEmpty must beFalse
    res.children.size must beEqualTo(1)
  }
}
