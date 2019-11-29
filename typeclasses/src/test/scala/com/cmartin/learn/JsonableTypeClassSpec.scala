package com.cmartin.learn

import com.cmartin.learn.MyTypeClasses.Jsonable._
import com.cmartin.learn.model.Constants._
import com.cmartin.learn.model.{ObfuscatedInt, ObfuscatedString, Person}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class JsonableTypeClassSpec extends AnyFlatSpec with Matchers {
  "Jsonable" should "serialize a Person" in {
    val person         = Person(name, firstName, ObfuscatedInt(33), id, ObfuscatedString(password))
    val result: String = serialize(person)

    result.nonEmpty shouldBe true
    result contains name shouldBe true
    result contains firstName shouldBe true
  }

  it should "serialize an Int" in {
    val int            = 1234
    val result: String = serialize(int)

    result.nonEmpty shouldBe true
    result contains String.valueOf(int) shouldBe true
  }

  it should "testSerializeDouble" in {
    val double: Double = 1234.4567
    val result         = serialize(double)

    result.nonEmpty shouldBe true
    result contains String.valueOf(double) shouldBe true
  }
}
