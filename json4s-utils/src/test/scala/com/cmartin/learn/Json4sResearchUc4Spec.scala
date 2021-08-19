package com.cmartin.learn

import com.cmartin.learn.ShadowService.CreateDto
import org.json4s.JsonAST.JValue

class Json4sResearchUc4Spec extends Json4sResearchBaseSpec {
  import Json4sResearch._
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 4"

  it should "return a list of key exclusions" in {
    // g i v e n
    val excludeElem: JValue = inputMessage_UC_4_1 \ metadataKey

    // w h e n
    val r = getExclusionKeys(excludeElem)

    // t h e n
    r shouldBe List("cts_ts", "aux")
  }

  it should "return a new shadow document with excluded fields, UC-4-1" in {
    // g i v e n
    val dto = CreateDto(0L, inputMessage_UC_4_1)
    val expectedShadow: JValue = shadowMessage_UC_4_1

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

  it should "return an updated shadow document with excluded @timestamp, UC-4-2" in {
    // g i v e n
    val dto = CreateDto(41L, inputMessage_UC_4_2)
    val expectedShadow: JValue = shadowMessage_UC_4_2

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

  it should "return an updated shadow document with excluded @timestamp and previous field, UC-4-3" in {
    // g i v e n
    val dto = CreateDto(42L, inputMessage_UC_4_3)
    val expectedShadow: JValue = shadowMessage_UC_4_3

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

}
