package com.cmartin.utils

import io.circe.Decoder.Result
import io.circe._
import io.circe.parser._

object JsonManager {

  import io.circe.generic.auto._

  case class Action(name: String, result: Int)

  def parse1(message: String): Either[ParsingFailure, Json] = parse(message)

  def getActions(json: Json): Result[Action] = {
    val cursor: HCursor = json.hcursor
    cursor.downField("actions").downArray.as[Action]
  }
}
