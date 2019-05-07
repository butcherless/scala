package com.cmartin

import org.json4s.DefaultFormats

package object learn {

  implicit val formats: DefaultFormats = org.json4s.DefaultFormats


  def buildPath(path: String, key: String): String =
    if (path.isEmpty) key else s"$path.$key"

  /*
    array size > 0
   */
  def buildArrayPath(path: String, index: Int): String = {
    val elem = s"[$index]"
    if (path.isEmpty) elem else s"$path.$elem"
  }

  val ArrayElem = """^\[(\d+)\]$""".r
}
