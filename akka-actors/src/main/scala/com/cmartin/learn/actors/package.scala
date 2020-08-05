package com.cmartin.learn

import java.util.UUID

import scala.util.Random

package object actors {

  /**
    * Sleeps the current thread for the milliseconds given as parameter
    *
    * @param ms milliseconds
    */
  def delay(ms: Long): Unit = {
    Thread.sleep(ms)
  }

  /**
    * Sleeps the current thread for a number of milliseconds between 5 and the
    * given parameter
    *
    * @param ms maximum milliseconds to delay
    * @return delay time
    */
  def delayUpTo(ms: Long): Long = {
    val random = Random.nextLong(ms)
    val delay  = if (random < 5) 5L else random
    Thread.sleep(delay)

    delay
  }

  /**
    * Generates a random number between 0 and 9
    *
    * @return the random number
    */
  def randomBetween0And9(): Int = Random.nextInt(10)

  def nextWorkerPosition(i: Int) = (i + 1) % 10

  def generateId(): String = UUID.randomUUID().toString
}
