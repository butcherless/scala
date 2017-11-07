package com.cmartin.learn

import com.cmartin.learn.MyTypeClasses.Jsonable
import com.cmartin.learn.MyTypeClasses.Show._
import com.typesafe.scalalogging.Logger

object App {
  val logger = Logger[App]

  def print[T](p: Person)(implicit j: Jsonable[Person]): String = j.serialize(p)

  def print[T](n: Int)(implicit j: Jsonable[Int]): String = j.serialize(n)

  def main(args: Array[String]) = {
    val message = "Hi from project learning TypeClasses in Scala!"
    val person = Person("Carlos", "Martin", "carlos.martin")

    logger.debug(message)

    logger.debug("scala-logging wrapper library debug level message!")
    logger.info("scala-logging wrapper library info level message!")


    logger.debug(print(person))
    logger.debug(print(1234))

    logger.debug(show(5))
    logger.debug(show(person))
    logger.debug(show(BigDecimal(1234.89)))
  }


}
