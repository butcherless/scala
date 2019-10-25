package com.cmartin.learn.common

import scala.Console.{BLUE, GREEN, RED, RESET}

object Utils {

  def prettyPrint[T](list: List[T]): String =
    list.mkString(s"list size (${list.size}) =>\n[\n", ",\n", "\n]")

  def colourRed(text: String): String =
    colour(text, RED)

  def colourGreen(text: String): String =
    colour(text, GREEN)

  def colourBlue(text: String): String =
    colour(text, BLUE)

  private def colour(text: String, color: String): String =
    s"$RESET$color$text$RESET"

}
