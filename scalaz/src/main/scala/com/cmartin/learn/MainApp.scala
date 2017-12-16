package com.cmartin.learn

import scalaz.Reader

object MainApp extends App {

  /**
  * dummy function, mod 16 of a char sequence of a string
  */
  def mod16(s:String):Int = {
    s.map(_.toInt).sum % 16
  }

  /** check if a hex number is a digit
  */
  def isDigit(n:Int):Boolean = n < 10

  /** check if a hex number is a characte
  */
  def isChar(n:Int):Boolean = !isDigit(n)

  /** converts a number to an hex Char
  */
  def toHex(n: Int): Char = n.toHexString.toCharArray.head.toUpper

  def hasModDigit(s:String):Boolean = isDigit(mod16(s))

  //val modR = Reader(mod16)

  /**
  * function composition: mod16 andThen toHex
  *
  * String => Int => Char, "a string" => 0..15 => '0'..'F'
  *
  * - The Reader Monad contains a function
  *
  * - map applies toHex function to the reader element which is the mod16
  * function
  *
  * - map returns another Reader and finally,
  *
  * Readar.run function is applied to the hexMod parameter 's'
  */
  def hexMod(s:String):Char = {
    val readerMonad = Reader(mod16).map(toHex)
    readerMonad(s)
  }

  println("Learning scalaz")

  val line = "scalaz-library-learning"
  val modOfLine = mod16(line)
  println(s"mod of: $line=" + modOfLine)

  val lineModIsDigit = isDigit(modOfLine)
  println(s"mod is digit: $lineModIsDigit")

  println(s"hasModDigit $line=" + hasModDigit(line))

  //val hexMod = Reader(mod16).map(toHex)

  println(s"reader.map(toHex): " + hexMod("pepe"))

}
