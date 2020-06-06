package com.cmartin.learn

import org.json4s.JsonAST.JNothing
import org.json4s.native.JsonMethods
import org.json4s.{DefaultFormats, JValue}

import scala.annotation.tailrec

object Json4sResearch {

  implicit val formats: DefaultFormats = org.json4s.DefaultFormats

  val XPATH_REGEX = """([a-z][a-z0-9]*)+([.][a-z][a-z0-9]*)*""".r

  def isXpath(path: String): Boolean =
    !path.isBlank && XPATH_REGEX.matches(path)

  def parse(jsonString: String): JValue =
    JsonMethods.parse(jsonString)

  def splitPath(path: String): Seq[String] = path.split('.').toList

  def getKey(path: String, json: JValue): JValue = {
    // loop function
    @tailrec
    def go(keys: Seq[String], json: JValue): JValue = {
      if (json.equals(JNothing) || keys.isEmpty)
        json
      else
        go(keys.tail, json \ keys.head)
    }

    if (isXpath(path))
      go(splitPath(path), json)
    else
      throw new RuntimeException(s"invalid xpath: $path")

  }
}
