package com.cmartin.utils

import io.circe
import io.circe.Decoder.Result
import io.circe.Json
import io.circe.parser._

object ZioLearn {
  sealed trait MyDomainException extends Exception

  case class MyExceptionOne(m: String) extends MyDomainException

  case class MyExceptionTwo(m: String) extends MyDomainException

  def refineError(): PartialFunction[Throwable, MyDomainException] = {
    case e: java.lang.IllegalArgumentException =>
      MyExceptionOne(s"refine illegal argument: [${e.getMessage}]")
    case e: java.lang.ArithmeticException      =>
      MyExceptionTwo(s"refine arithmetic error: [${e.getMessage}]")
  }

  def parseMessage(message: String): Either[circe.Error, Json] = parse(message)

  def getValue(json: Json, key: String): Result[Json] =
    json.hcursor.downField(key).as[Json]
}
