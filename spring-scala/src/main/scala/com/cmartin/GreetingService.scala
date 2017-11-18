package com.cmartin

import org.springframework.stereotype.Service

import scala.util.Random

@Service
class GreetingService {

  def generateRandom(n: Int): Int = {
    if (n <= 0)
      0
    else {
      val r = Random.nextInt(10) + 1
      if (r == n)
        r % 10 + 1
      else
        r
    }
  }
}
