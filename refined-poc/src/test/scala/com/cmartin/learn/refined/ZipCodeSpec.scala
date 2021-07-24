package com.cmartin.learn.refined

class ZipCodeSpec extends PropertySpec {

  val zipCodes = Table("code", "28020", "01000", "52900")
  val invalidZipCodes = Table("code", "00999", "53001", "2800X")

  property("zipCodes should contains valid zip code values") {
    forAll(zipCodes) { code =>
      validateZipCode(code) contains code
    }
  }

  property("nonLeapYears should contains invalid leap year values") {
    forAll(invalidZipCodes) { code =>
      validateZipCode(code).isLeft shouldBe true
    }
  }

}
