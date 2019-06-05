package com.cmartin.learn

import com.cmartin.learn.model.Constants._
import com.cmartin.learn.model.{ ObfuscatedInt, ObfuscatedString, Person }
import utest._

object ObfuscableTests extends TestSuite {

  val tests = Tests {
    'obfuscateString - {
      val text = "chiquito"
      val result = ObfuscatedString(text).toString

      assert(
        !result.isEmpty(),
        result.length == text.length)
    }

    'obfuscateInt - {
      val number = 123
      val result = ObfuscatedInt(number).toString

      assert(
        !result.isEmpty(),
        result.length == 3)
    }

    'obfuscatePerson - {
      val person = Person(name, firstName, ObfuscatedInt(age), id, ObfuscatedString(password))

      val result = person.toString
      val ageResult = person.age.toString
      val passwordResult = person.password.toString

      assert(
        !result.isEmpty(),
        ageResult == "*" * 2,
        passwordResult == "*" * 8)
    }
  }
}

