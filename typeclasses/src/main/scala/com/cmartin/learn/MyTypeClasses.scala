package com.cmartin.learn

import com.cmartin.learn.model.Person

object MyTypeClasses {

  /**
    * Created by cmartin on 26/12/2016.
    */
  // Jsonable typeclass

  trait Jsonable[T] {
    def serialize(t: T): String
  }

  object Jsonable {

    def serialize[T](t: T)(implicit ev: Jsonable[T]): String = ev.serialize(t)

    implicit val JsonableInt = new Jsonable[Int] {
      override def serialize(x: Int): String = {
        s"""Int={"value":${x.toString}}"""
      }
    }

    implicit val JsonablePerson = new Jsonable[Person] {
      override def serialize(x: Person): String = {
        s"""Person={"name":"${x.name}", "firstname":"${x.firstName}", "id":"${x.id}"}"""
      }
    }

    implicit def doubleInstance = new Jsonable[Double] {
      override def serialize(x: Double): String = {
        "{ \"value\":" + x.toString + "}"
      }
    }

  }

  // Show typeclass

  trait Show[T] {
    def show(t: T): String
  }

  object Show {
    def show[T](t: T)(implicit ev: Show[T]) = ev.show(t)

    implicit val showInt: Show[Int] =
      new Show[Int] {
        override def show(a: Int): String = s"integer: ${a}"
      }

    implicit val showLong: Show[Long] =
      (a: Long) => s"long: ${a}"

    implicit val showBigDecimal: Show[BigDecimal] =
      (a: BigDecimal) => s"bigDecimal: ${a}"

    implicit val showPerson: Show[Person] =
      new Show[Person] {
        override def show(t: Person): String = s"person: ${t.id}, ${t.name}, ${t.firstName}"
      }
  }

}
