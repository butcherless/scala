package com.cmartin.impl

import com.cmartin.Person
import com.cmartin.algebra.NumberWord
import org.scalatest.FunSuite

class ServiceTests extends FunSuite {
  val service = new GreetingServiceImpl

  val ID = 64
  val FIRSTNAME = "firstname"
  val LASTNAME = "lastname"
  val EMAIL = "email"

  test("testConvertDecimalnumberToWord") {
    assert(service.convertDecimalnumberToWord(0).getOrElse(NumberWord.invalid).equals(NumberWord.zero))
    assert(service.convertDecimalnumberToWord(1).getOrElse(NumberWord.invalid).equals(NumberWord.one))
    assert(service.convertDecimalnumberToWord(2).getOrElse(NumberWord.invalid).equals(NumberWord.two))
    assert(service.convertDecimalnumberToWord(3).getOrElse(NumberWord.invalid).equals(NumberWord.three))
    assert(service.convertDecimalnumberToWord(4).getOrElse(NumberWord.invalid).equals(NumberWord.four))
    assert(service.convertDecimalnumberToWord(5).getOrElse(NumberWord.invalid).equals(NumberWord.five))
    assert(service.convertDecimalnumberToWord(6).getOrElse(NumberWord.invalid).equals(NumberWord.six))
    assert(service.convertDecimalnumberToWord(7).getOrElse(NumberWord.invalid).equals(NumberWord.seven))
    assert(service.convertDecimalnumberToWord(8).getOrElse(NumberWord.invalid).equals(NumberWord.eight))
    assert(service.convertDecimalnumberToWord(9).getOrElse(NumberWord.invalid).equals(NumberWord.nine))
    assert(service.convertDecimalnumberToWord(10).getOrElse(NumberWord.invalid).equals(NumberWord.ten))
  }

  test("testInvalidNumber") {
    assert(service.convertDecimalnumberToWord(-11).isFailure)
  }

  test("testGenerateRandomPair") {
    val pair = service.generateRandomPair(1, 10)
    assert(pair.source == NumberWord.one)
    assert(pair.target != NumberWord.one)
    assert(pair.limit == 10)
  }

  test("testPersonBean") {
    val p = new Person(ID, FIRSTNAME, LASTNAME, EMAIL)
    assert(p.id == ID)
    assert(p.firstName == FIRSTNAME)
    assert(p.lastName == LASTNAME)
    assert(p.email == EMAIL)
  }
}
