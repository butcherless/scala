package com.cmartin


object App {

  import MyTypeClasses.Jsonable

  def print[T](p: Person)(implicit j: Jsonable[Person]): String = j.serialize(p)
  def print[T](n: Int)(implicit j: Jsonable[Int]): String = j.serialize(n)

  def main(args: Array[String]) = {
    val message = "Hi from project learning TypeClasses in Scala!"
    val person = Person("Carlos", "Martin", "carlos.martin")

    println(message)
    println(print(person))
    println(print(1234))
  }


}
