package com.cmartin.learn.refined

class PersonNameSpec extends PropertySpec {

  val personNames        = Table(
    "name",
    "Robben Ford",
    "Katherine Jones-Smith",
    "Kristin Scott Thomas"
  )
  val invalidPersonNames =
    Table("name", "Robben , Ford", "Roscoe Bec3", "Andrew Mar$hall")

  property("personNames should contains valid person name values") {
    forAll(personNames) { name =>
      validatePersonName(name) contains name
    }
  }

  property("invalidPersonNames should contains invalid person name values") {
    forAll(invalidPersonNames) { name =>
      validatePersonName(name).isLeft shouldBe true
    }
  }

}
