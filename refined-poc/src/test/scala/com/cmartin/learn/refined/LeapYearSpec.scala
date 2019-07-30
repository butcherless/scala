package com.cmartin.learn.refined

class LeapYearSpec extends PropertySpec {

  val leapYears = Table("year", 1600, 1992, 2000, 2020)
  val invalidLeapYears = Table("year", 1700, 1800, 2002, 2100)

  property("leapYears should contains valid leap year values") {
    forAll(leapYears) { year =>
      validateLeapYear(year) contains year
    }
  }

  property("nonLeapYears should contains invalid leap year values") {
    forAll(invalidLeapYears) { year =>
      validateLeapYear(year).isLeft shouldBe true
    }
  }

}
