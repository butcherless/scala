package com.cmartin.learn

import com.cmartin.learn.model.Constants._
import com.cmartin.learn.model.{ObfuscatedInt, ObfuscatedString, Person}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

object ObfuscableSpec extends AnyFlatSpec with Matchers {
  "Obfuscable" should "obfuscate a String" in {
    val text   = "chiquito"
    val result = ObfuscatedString(text).toString

    result.nonEmpty shouldBe true
    result.length shouldBe text.length
  }

  it should "obfuscate an Int" in {
    val number = 123
    val result = ObfuscatedInt(number).toString

    result.nonEmpty shouldBe true
    result.length shouldBe 3
  }

  it should "obfuscate a Person" in {
    val person = Person(name, firstName, ObfuscatedInt(age), id, ObfuscatedString(password))

    val result         = person.toString
    val ageResult      = person.age.toString
    val passwordResult = person.password.toString

    result.nonEmpty shouldBe true
    ageResult shouldBe "*" * 2
    passwordResult shouldBe "*" * 8
  }
}
