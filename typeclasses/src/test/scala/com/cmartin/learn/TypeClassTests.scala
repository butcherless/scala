package com.cmartin.learn

import com.cmartin.MyTypeClasses.Show._
import com.cmartin.Person
import utest._

object TypeClassTests extends TestSuite {
  val name = "Carlos"
  val firstName = "Martin"
  val id = "carlos.martin"


  val tests = Tests {
    'testShowPerson - {
      val person = Person(name, firstName, id)
      val s = show(person)

      assert(!s.isEmpty(),
        s.contains(getNameToLower(person)),
        s.contains(name),
        s.contains(firstName),
        s.contains(id))
    }

    'testShowInt - {
      val int = 1234
      val s = show(int)

      assert(!s.isEmpty(),
        s.contains(getNameToLower(int)),
        s.contains(String.valueOf(int))
      )
    }

    'testShowLong - {
      val long: Long = 1234
      val s = show(long)

      assert(!s.isEmpty(),
        s.contains(getNameToLower(long)),
        s.contains(String.valueOf(long))
      )
    }

    'testShowBigDecimal - {
      val bd: BigDecimal = BigDecimal.apply(1234.56)
      val s = show(bd)

      assert(!s.isEmpty(),
        s.toLowerCase.contains(getNameToLower(bd)),
        s.contains(String.valueOf(bd))
      )
    }

  }

  def getNameToLower(c: Any) = c.getClass.getSimpleName.toLowerCase

}
