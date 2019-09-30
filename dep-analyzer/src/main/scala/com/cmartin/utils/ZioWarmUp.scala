package com.cmartin.utils

import com.cmartin.utils.JsonManager.Action
import zio.{IO, UIO}

object ZioWarmUp {
  val jsonKeys: List[String] = List("k1", "k2", "k3")
  val jsonDoc: Json = Json(jsonKeys)

  sealed trait JsonException extends Exception

  case class ParsingException(m: String) extends JsonException

  case class MappingException(m: String) extends JsonException

  sealed trait JsonError

  case class ParsingError(m: String) extends JsonError

  case class MappingError(m: String) extends JsonError

  case class UnknownError(m: String) extends JsonError


  case class Json(keys: List[String])

  def sum(a: Int, b: Int): UIO[Int] =
    UIO.succeed(a + b)

  def simulateParseJson(message: String): UIO[Either[Throwable, Json]] = {

    def parse(message: String): Json = {
      if (message.isEmpty) throw new RuntimeException("parsing exception")
      else Json(jsonKeys)
    }

    IO.effect {
      parse(message)
    }.either
  }


  def simulateJsonMapping(message: String): UIO[Either[JsonError, Action]] = {

    def extract(message: String): Action = {
      if (message.isEmpty) throw MappingException("invalid type")
      else Action(message, 0)
    }

    IO.effect {
      extract(message)
    }.mapError {
      case e: ParsingException => ParsingError("invalid format")
      case e: MappingException => MappingError("invalid type")
      case e: Throwable => UnknownError("error while processing json message")
    }.either
  }


  val validMessage =
    """
      |{
      |  "id": "abc-123-efg-456-xyz",
      |  "actions": [
      |    {
      |      "name": "action-1",
      |      "result": 1
      |    },
      |    {
      |      "name": "action-2",
      |      "result": 2
      |    }
      |  ]
      |}
      |""".stripMargin
}
