package com.cmartin.learn

import com.cmartin.learn.MyTypeClasses.Jsonable._
import com.cmartin.learn.model.Constants._
import com.cmartin.learn.model.{ObfuscatedInt, ObfuscatedString, Person}
import utest._

object JsonableTypeClassTests extends TestSuite {

  val tests = Tests {
    'testSerializePerson - {
      val person = Person(name, firstName, ObfuscatedInt(33), id, ObfuscatedString(password))
      val s = serialize(person)

      assert(!s.isEmpty(),
        //  s.contains(getNameToLower(person)),
        s.contains(name),
        s.contains(firstName)
        //s.contains(id)
      )
    }

    'testSerializeInt - {
      val int = 1234
      val s = serialize(int)

      assert(!s.isEmpty(),
        s.contains(String.valueOf(int))
      )
    }

    'testSerializeDouble - {
      val double: Double = 1234.4567
      val s = serialize(double)

      assert(!s.isEmpty(),
        s.contains(String.valueOf(double)),
        s.contains('.')
      )
    }
  }

  def getNameToLower(c: Any) = c.getClass.getSimpleName.toLowerCase

}
