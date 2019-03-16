package com.cmartin.learn.refined

class PersonNameSpec extends PropertySpec {

  val personNames = Table("name", "Robben Ford", "Katherine Jones-Smith", "Kristin Scott Thomas")
  val invalidPersonNames = Table("name", "Robben , Ford", "Roscoe Bec3", "Andrew Mar$hall")

  property("zipCodes should contains valid zip code values") {
    forAll(personNames) {
      name => validatePersonName(name).isRight shouldBe true
    }
  }

  property("nonLeapYears should contains invalid leap year values") {
    forAll(invalidPersonNames) {
      name => validatePersonName(name).isLeft shouldBe true
    }
  }

}
