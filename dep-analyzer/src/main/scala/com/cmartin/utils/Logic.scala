package com.cmartin.utils

object Logic {

  import scala.Console.{MAGENTA, RED, RESET, YELLOW}

  type GAV = (String, String, String)

  /**
    * Se obtiene "group:artifact" que se usan como clave (K) de un Map para
    * recuperar el valor (V) que es lista de versiones de dicho par.
    */

  def getGroupAndArtifact(gav: GAV): String = {
    s"${gav._1}:${gav._2}"
  }

  def getVersion(gav: GAV): String = {
    s"${gav._3}"
  }

  def mkString(k: String, l: List[String]) = {
    val s = l.mkString(", ")
    s"${RESET}${YELLOW}$k${RESET} (${RESET}${MAGENTA}${l.size}${RESET}) => [${RESET}${MAGENTA}$s${RESET}]"
  }

  def getGAV(s: String): Option[GAV] = {
    val gavArray = s.split(":")
    if (gavArray.length == 3)
      Some((gavArray(0), gavArray(1), gavArray(2)))
    else
      None
  }

  def mkErrorString(s: String): String = {
    s"dependency format error => ${RESET}${RED}$s${RESET}"
  }

}
