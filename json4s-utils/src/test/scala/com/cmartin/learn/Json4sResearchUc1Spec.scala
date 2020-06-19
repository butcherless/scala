package com.cmartin.learn

import com.cmartin.learn.Json4sResearch._
import org.json4s.JValue
import org.json4s.JsonAST.JNothing
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Json4sResearchUc1Spec extends AnyFlatSpec with Matchers {
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 1"

  /*
     - no t_field
     - no @timestamp
   */

  it should "return a new flattened and ISO dated message, UC-1-1" in {
    // simulates find at Repository
    val currentShadow                = JNothing
    val expectedMergedShadow: JValue = parse(shadowMessage_UC_1_1)

    val payload = parse(inputMessage_UC_1_1) \ payloadKey
    // date: no timestamp => now()
    val metadata       = createMetadata(payload, dateText)
    val incomingShadow = createShadow(payload, metadata)
    val mergedShadow   = mergeShadows(currentShadow, incomingShadow)

    mergedShadow shouldBe expectedMergedShadow
  }

  it should "return an updated, flattened and ISO dated message, UC-1-2" in {
    // simulates find at Repository
    val currentShadow                = parse(shadowMessage_UC_1_1)
    val expectedMergedShadow: JValue = parse(outputMessage_UC_1_2)

    val payload = parse(inputMessage_UC_1_2) \ payloadKey
    // date: no timestamp => now()
    val metadata       = createMetadata(payload, dateText2)
    val incomingShadow = createShadow(payload, metadata)
    val mergedShadow   = mergeShadows(currentShadow, incomingShadow)

    mergedShadow shouldBe expectedMergedShadow
  }

}
