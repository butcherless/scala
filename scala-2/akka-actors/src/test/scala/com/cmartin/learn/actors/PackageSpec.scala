package com.cmartin.learn.actors

import com.cmartin.learn.actors.PackageSpec._
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable.Queue

class PackageSpec extends AnyFlatSpec {
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

  it should "not remove any processed element" in {
    val messageSeq = Seq(mnp1, mnp2, mp1, mp2, mp3)
    var messageQueue: Queue[Message] = Queue.from(messageSeq)

    assert(messageQueue.nonEmpty)
    // processed removal loop, first message is not processed
    messageQueue = messageQueue.dropWhile(message => message.processed)
    assert(messageQueue.size == messageSeq.size)
  }

  it should "remove the longest prefix (3) of processed elements" in {
    val messageSeq = Seq(mp1, mp2, mp3, mnp1, mnp2)
    var messageQueue: Queue[Message] = Queue.from(messageSeq)

    assert(messageQueue.nonEmpty)
    messageQueue = messageQueue.dropWhile(message => message.processed)
    assert(messageQueue.size == (messageSeq.size - 3))
  }
}

object PackageSpec {
  case class Message(payload: Int, processed: Boolean)

  val mnp1 = Message(1, false)
  val mnp2 = Message(2, false)
  val mp1 = Message(3, true)
  val mp2 = Message(4, true)
  val mp3 = Message(5, true)
}
