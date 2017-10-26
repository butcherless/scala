package com.cmartin


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

}
