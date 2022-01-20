package com.cmartin.learn

import java.util.Date

import com.cmartin.learn.Domain.DummyMessage
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class SimpleAppSpec extends AnyFlatSpec with Matchers {
  "Application Helper" should "build a JSON DummyMessage" in {
    val timestamp: Long = new Date().getTime()
    val dummyMessage    = DummyMessage(1, "dummy", timestamp, 7)
    val json: String    = AppHelper.buildDummyMessage(dummyMessage)

    json shouldBe s"""{"id":1,"text":"dummy","timestamp":$timestamp,"key":7}"""
  }

  it should "generate a random string" in {
    val randomString = Random.alphanumeric.take(64).mkString
    info(randomString)

    randomString should have length 64
    randomString.forall(ch => ch.isLetterOrDigit) shouldBe true
  }
}
