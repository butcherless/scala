package com.cmartin.learn.actors

import org.scalatest.FlatSpec

import scala.collection.mutable.Queue

class PackageSpec extends FlatSpec {

  "PackageSpec" should "verify random range between 0 and 9" in {
    val MAX_VALUE = 1000000

    for (i <- 0 to MAX_VALUE) {
      val rn = randomBetween0And9()
      assert(rn >= 0)
      assert(rn < 10)
    }
  }

  it should "check Queue operations" in {
    val digitSet = (0 to 9).toSet
    val queue: Queue[Int] = Queue.empty[Int]

    while (queue.size < 10) {
      val n = randomBetween0And9()
      if (!queue.contains(n)) queue += n
    }
    //    assert(queue.isEmpty)
    //    queue += 2
    //    assert(queue.contains(2))
    //    queue += 7
    //    assert(queue.contains(7))
    //    info(queue.toString())


    info(queue.toString())
    assert(digitSet == queue.toSet)
  }
}
