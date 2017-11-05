package com.cmartin.learn


object MyTypeClasses {

  /**
    * Created by cmartin on 26/12/2016.
    */
  trait Jsonable[T] {
    def serialize(x: T): String
  }

  object Jsonable {

    implicit object JsonableInt extends Jsonable[Int] {
      override def serialize(x: Int): String = {
        //TODO improve implementation
        s"""Int={"value":${x.toString}}"""
      }
    }

    implicit object JsonablePerson extends Jsonable[Person] {
      override def serialize(x: Person): String = {
        //TODO improve implementation
        s"""Person={"name":"${x.name}", "firstname":"${x.firstName}", "id":"${x.id}"}"""
      }
    }

    /*
        implicit def intInstance[T](implicit ev: Jsonable[T]) = new Jsonable[Int] {
          override def serialize(x: Int): String = {
            //TODO improve implementation
            "{ \"value\":" + x.toString + "}"
          }
        }

        implicit def doubleInstance[T](implicit ev: Jsonable[T]) = new Jsonable[Double] {
          override def serialize(x: Double): String = {
            //TODO improve implementation
            "{ \"value\":" + x.toString + "}"
          }
        }

        implicit def personInstance[T](implicit ev: Jsonable[T]) = new Jsonable[Person] {
          override def serialize(x: Person): String = {
            //TODO improve implementation
            "{ \"name\":\"" + x.name + "\"" +
              ",\"firstname\":\"" + x.firstName + "\"" +
              ",\"id\":\"" + x.id + "\"}"
          }
        }
    */
  }

  trait Show[T] {
    def show(t: T): String
  }

  object Show {
    def show[T](t: T)(implicit s: Show[T]) = s.show(t)

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
