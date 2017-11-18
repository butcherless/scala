package com.cmartin

import org.springframework.stereotype.Service

import scala.util.Random

@Service
class GreetingService {

  /**
    * Generates a random number between 1 and limit excluding n
    *
    * @param n     number to be excluded
    * @param limit max number to be returned
    * @return the random number
    */
  def generateRandom(n: Int, limit: Int): Int = {
    if (n <= 0)
      0
    else {
      val r = Random.nextInt(limit) + 1
      if (r == n)
        r % limit + 1
      else
        r
    }
  }
}
