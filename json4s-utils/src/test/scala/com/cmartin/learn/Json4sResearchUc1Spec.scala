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
   - no @timestamp
   - no t_field
   */

  it should "return a new shadow message with now() date, UC-1-1" in {
    // simulates searching the repository
    val currentShadow                = JNothing
    val expectedMergedShadow: JValue = shadowMessage_UC_1_1

    val payload = inputMessage_UC_1_1 \ payloadKey
    // date: no timestamp => now()
    val metadata       = createMetadata(payload, dateText)
    val incomingShadow = createShadow(payload, metadata)
    val mergedShadow   = mergeShadows(currentShadow, incomingShadow)

    mergedShadow shouldBe expectedMergedShadow
  }

  it should "return an updated shadow message with now() date, UC-1-2" in {
    // simulates searching the repository
    val currentShadow                = shadowMessage_UC_1_1
    val expectedMergedShadow: JValue = shadowMessage_UC_1_2

    val payload = parse(inputMessage_UC_1_2) \ payloadKey
    // date: no timestamp => now()
    val metadata       = createMetadata(payload, dateText2)
    val incomingShadow = createShadow(payload, metadata)
    val mergedShadow   = mergeShadows(currentShadow, incomingShadow)

    mergedShadow shouldBe expectedMergedShadow
  }

}
