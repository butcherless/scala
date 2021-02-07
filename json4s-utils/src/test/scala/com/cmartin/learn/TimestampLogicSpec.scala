package com.cmartin.learn

import com.cmartin.learn.Json4sResearch._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.matching.Regex

class TimestampLogicSpec extends AnyFlatSpec with Matchers {

  import Json4sResearchTestUtils._
  import TimestampLogicSpec._

  behavior of "Timestamp logic"

  it should "return now() date for missing timestamp and t_field" in {
    val timestamp: String = resolveTimestamp(inputMessage_UC_1_1).getOrElse("")
    //info(timestamp)

    timestamp should fullyMatch regex datePattern
  }

  it should "return the inputMessage.payload @timestamp property value" in {
    val timestamp: String = resolveTimestamp(inputMessage_UC_2_1).getOrElse("")
    //info(timestamp)

    timestamp shouldBe "2020-06-10T04:21:13Z"
  }

  it should "return the inputMessage.payload cts_ts property value" in {
    val timestamp: String = resolveTimestamp(inputMessage_UC_3_1).getOrElse("")
    info(timestamp)

    timestamp shouldBe "2020-06-10T04:21:13Z"
  }

}

object TimestampLogicSpec {

  val datePattern: Regex = """[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.*Z""".r
}
