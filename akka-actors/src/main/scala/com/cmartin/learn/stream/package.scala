package com.cmartin.learn

import akka.stream.scaladsl.Source

import scala.concurrent.duration._
import scala.util.Random

package object stream {

  def buildIntSource(n: Int) = {
    Source(1 to n).throttle(1, 1 second)
  }

  /**
    * Generates a random number between 0 and MaxValue
    *
    * @return the random number
    */
  def randomPositiveInt(): Int = Math.abs(Random.nextInt())
}
