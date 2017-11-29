package com.cmartin.algebra

import com.cmartin.SourceTargetPair

import scala.util.Try

trait GreetingService {
  /**
    * Converts a number in the range [1,10] to its word
    *
    * @param n a number between 1 and 10
    * @return word representation of the number
    */
  def convertDecimalnumberToWord(n: Int): Try[String]

  /**
    * Generates a random number between 1 and limit excluding n
    *
    * @param n     number to be excluded
    * @param limit max number to be returned
    * @return the random number
    */
  def generateRandom(n: Int, limit: Int): Try[Int]

  def generateRandomPair(n: Int, limit: Int): Try[SourceTargetPair]
}
