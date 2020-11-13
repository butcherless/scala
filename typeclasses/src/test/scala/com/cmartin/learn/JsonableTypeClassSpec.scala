package com.cmartin.learn

import com.cmartin.learn.MyTypeClasses.Jsonable.Syntax._
import com.cmartin.learn.MyTypeClasses.Jsonable._
import com.cmartin.learn.model.Constants._
import com.cmartin.learn.model.{ObfuscatedInt, ObfuscatedString, Person}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class JsonableTypeClassSpec extends AnyFlatSpec with Matchers {

  behavior of "Jsonable type class"

  it should "serialize a Person" in {
    val person         = Person(name, firstName, ObfuscatedInt(33), id, ObfuscatedString(password))
    val result: String = serialize(person)

    result.nonEmpty shouldBe true
    result contains name shouldBe true
    result contains firstName shouldBe true
  }

  it should "serialize an Int" in {
    val integer        = 1234
    val result: String = serialize(integer)

    result shouldBe """Int={"value":1234}"""
  }

  it should "serialize a double" in {
    val double: Double = 1234.5678
    val result         = serialize(double)

    result shouldBe """Double={"value":1234.5678}"""
  }

  it should "serialize an integer via syntax" in {
    val integer        = 1234
    val result: String = integer.serialize

    info(result)

    result shouldBe """Int={"value":1234}"""
  }

  it should "serialize a double via syntax" in {
    val double         = 1234.5678
    val result: String = double.serialize

    info(result)

    result shouldBe """Double={"value":1234.5678}"""
  }

}
