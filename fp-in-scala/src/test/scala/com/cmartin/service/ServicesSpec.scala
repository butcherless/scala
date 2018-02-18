package com.cmartin.service

import org.specs2.mutable.Specification

class ServicesSpec extends Specification {

  "should generate an Int between 1 and 10 for Color class" >> {
    val color = Color("red")
    val res = Services.calcHashId(color)
    res must beGreaterThanOrEqualTo(1) and beLessThanOrEqualTo(10)
  }

  "color repository should not be empty" >> {
    val repo = new ColorRepository
    val res = repo.colorCount()
    res must not be equalTo(0)
  }

}
