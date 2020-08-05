package com.cmartin.dto

import com.cmartin.dto.Plane.buildPlane
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.HashSet

class DtoSpec extends AnyFlatSpec with Matchers {
  val ID           = 1234
  val REGISTRATION = "EC-LXM"
  val MODEL        = "350-800"
  val EMPTY_STRING = ""

  def plane2Tuple(p: Plane): (Long, String, String, String) = Plane.unapply(p).get

  it should "mandatory constructor arguments" in {
    val tuple    = plane2Tuple(Plane(ID, REGISTRATION))
    val resTuple = (ID, REGISTRATION, EMPTY_STRING, EMPTY_STRING)
    resTuple shouldBe tuple
  }

  it should "named constructor arguments" in {
    val tuple    = plane2Tuple(Plane(ID, REGISTRATION, model = MODEL))
    val resTuple = (ID, REGISTRATION, EMPTY_STRING, MODEL)
    resTuple shouldBe tuple
  }

  it should "should return a valid Plane" in {
    val res = buildPlane(ID, REGISTRATION)
    res.isDefined shouldBe true
  }

  it should "should return None for an invalid id" in {
    val res = buildPlane(-1, REGISTRATION)
    res.isDefined shouldBe false
  }

  it should "should return None for an invalid registration" in {
    val res = buildPlane(ID, "")
    res.isDefined shouldBe false
  }

  it should "should be empty collection" in {
    val res = Parent(ID, "parent description", HashSet())
    res.id shouldBe ID
    res.desc.isEmpty shouldBe false
    res.children.isEmpty shouldBe true
  }

  it should "should be non empty collection" in {
    val child = Child(ID, "child description")
    val res   = Parent(ID, "parent description", HashSet(child))
    res.id shouldBe ID
    res.desc.isEmpty shouldBe false
    res.children.isEmpty shouldBe false
    res.children.size shouldBe 1
  }
}
