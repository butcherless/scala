package com.cmartin.learn

import com.cmartin.learn.MyTypeClasses.Show._
import com.cmartin.learn.model.Constants._
import com.cmartin.learn.model.{ObfuscatedInt, ObfuscatedString, Person}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TypeClassSpec extends AnyFlatSpec with Matchers {
  "Type class" should "show a Person" in {
    val person = Person(
      name,
      firstName,
      ObfuscatedInt(age),
      id,
      ObfuscatedString(password)
    )
    val result = show(person)

    result.nonEmpty shouldBe true
    result.contains(getNameToLower(person)) shouldBe true
    result.contains(name) shouldBe true
    result.contains(firstName) shouldBe true
    result.contains(id) shouldBe true
  }

  it should "show an Int" in {
    val int    = 1234
    val result = show(int)

    result.nonEmpty shouldBe true
    result.contains(getNameToLower(int)) shouldBe true
    result.contains(String.valueOf(int)) shouldBe true
  }

  it should "show a Long" in {
    val long: Long = 1234
    val result     = show(long)

    result.nonEmpty shouldBe true
    result.contains(getNameToLower(long)) shouldBe true
    result.contains(String.valueOf(long)) shouldBe true
  }

  it should "show a BigDecimal" in {
    val bd: BigDecimal = BigDecimal(1234.56)
    val result         = show(bd)

    result.nonEmpty shouldBe true
    result.contains(String.valueOf(bd)) shouldBe true
  }

  def getNameToLower(c: Any) = c.getClass.getSimpleName.toLowerCase
}
