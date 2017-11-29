package com.cmartin.impl

import com.cmartin.SourceTargetPair
import com.cmartin.algebra.GreetingService
import org.springframework.stereotype.Service

import scala.util.{Failure, Random, Success, Try}

@Service
class GreetingServiceImpl extends GreetingService {

  override def generateRandom(n: Int, limit: Int): Try[Int] = {
    if (n <= 0)
      Success(0)
    else {
      val r = Random.nextInt(limit) + 1
      if (r == n)
        Success(r % limit + 1)
      else
        Success(r)
    }
  }

  override def convertDecimalnumberToWord(n: Int): Try[String] = {
    n match {
      case 0 => Success("zero")
      case 1 => Success("one")
      case 2 => Success("two")
      case 3 => Success("three")
      case 4 => Success("four")
      case 5 => Success("five")
      case 6 => Success("six")
      case 7 => Success("seven")
      case 8 => Success("eight")
      case 9 => Success("nine")
      case 10 => Success("ten")

      case _ => Failure(new IllegalArgumentException(s"Invalid number $n"))
    }
  }

  override def generateRandomPair(n: Int, limit: Int): Try[SourceTargetPair] = {
    for {
      random <- generateRandom(n, limit)
      source <- convertDecimalnumberToWord(n)
      target <- convertDecimalnumberToWord(random)
    } yield SourceTargetPair(source, target, limit)
  }
}
