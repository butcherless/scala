package com.cmartin.learn

import java.time.{ZoneId, ZonedDateTime}

import com.cmartin.learn.Json4sResearch.{createMetadata, createShadow, mergeShadows, _}
import org.json4s.{JValue, JsonAST}
import org.json4s.JsonAST.{JField, JNothing, JObject, JString, JValue}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Json4sResearchUc2Spec extends AnyFlatSpec with Matchers {
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 2"

  /*
   - with @timestamp
   - no t_field
   */

  it should "return a new shadow message with @timestamp date, UC-2-1" in {
    // simulates searching the repository
    val currentShadow                = JNothing
    val expectedMergedShadow: JValue = shadowMessage_UC_2_1

    val payload = inputMessage_UC_2_1 \ payloadKey
    // date: timestamp
    val timestampEither = resolveTimestamp(payload)
    val timestamp       = timestampEither.getOrElse(fail("invalid timestamp"))

    val metadata       = createMetadata(payload, timestamp)
    val incomingShadow = createShadow(payload, metadata)
    val mergedShadow   = mergeShadows(currentShadow, incomingShadow)

    mergedShadow shouldBe expectedMergedShadow
  }

  it should "TODO , UC-2-2" in {
    // simulates searching the repository
    val currentShadow                = shadowMessage_UC_2_1
    val expectedMergedShadow: JValue = shadowMessage_UC_2_2

    val payload = inputMessage_UC_2_2 \ payloadKey
    // date: outdated timestamp => ignore
    val timestampEither = resolveTimestamp(payload)
    val timestamp       = timestampEither.getOrElse(fail("invalid timestamp"))

    val shadowTimestamp = getShadowTimestamp(currentShadow)

    val b = isNewer(timestamp, shadowTimestamp.getOrElse(fail("invalid timestamp")))
    info("greater: " + b)

    /* TODO case missing date, JNothing
            createShadow operation , whole operation
            createShadowEntity (structure)
     */

  }

}
