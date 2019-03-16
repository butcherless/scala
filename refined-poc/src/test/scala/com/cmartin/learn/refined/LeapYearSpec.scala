package com.cmartin.learn.refined

import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.TableDrivenPropertyChecks

class LeapYearSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val leapYears = Table("year", 1600, 1992, 2000, 2020)
  val nonLeapYears = Table("year", 1700, 1800, 2002, 2100)

  property("CURRENT leapYears should contains valid leap year values") {
    forAll(leapYears) {
      year => validateLeapYear(year).isRight shouldBe true
    }
  }

  property("CURRENT nonLeapYears should contains invalid leap year values") {
    forAll(nonLeapYears) {
      year => validateLeapYear(year).isLeft shouldBe true
    }
  }

}
