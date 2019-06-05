package com.cmartin.learn

import com.cmartin.learn.generator._
import com.typesafe.scalalogging.Logger

import scala.util.{ Failure, Success }

object MainApp extends App {
  val logger = Logger[App]

  logger.debug("scala-logging wrapper library debug level message!")
  logger.info("scala-logging wrapper library info level message!")

  // ALGEBRA

  println("\nDtoGenerator algebra!")
  val typeRep = TryDtoGenerator.create(Type("mypkg", "MyTpe"))
  typeRep match {
    case Success(s) => println(s"TypeRepresentation[type: ${s.t}, file: ${s.f}]")
    case Failure(f) => println(s"function create type " + f.toString)

  }

  val sb = CommonsLang3

  val tr = TypeRepresentation(Type("pkg", "name"), File("dir", "name"))
    .withPackage("name")
    .withStringBuilder(sb)

  val dto = DTO("transfer")
    .withSerializable
    .withToString
    .withProperty(PROP("sender", STRING) isMandatory (true))
    .withProperty(PROP("receiver", STRING) isMandatory (true))
    .withProperties(List(
      PROP("amount", BIGDECIMAL),
      PROP("currency", STRING)))
    .withGetters
    .build
}
