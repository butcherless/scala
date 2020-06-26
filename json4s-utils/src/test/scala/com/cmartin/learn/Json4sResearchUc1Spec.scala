package com.cmartin.learn

import com.cmartin.learn.Json4sResearch._
import com.cmartin.learn.ShadowService.CreateDto
import org.json4s.JsonAST.JValue

class Json4sResearchUc1Spec extends Json4sResearchBaseSpec {
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 1"

  /*
   - inDto.payload
   - no @timestamp
   - no t_field
   */

  it should "return a new shadow message with now() date, UC-1-1" in {
    // g i v e n
    val dto = CreateDto(0L, inputMessage_UC_1_1)

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Symbol("right")
    updatedShadow.map { json =>
      (json \ metadataKey).children.size shouldBe (shadowMessage_UC_1_1 \ metadataKey).children.size
      (json \ stateKey) shouldBe (shadowMessage_UC_1_1 \ stateKey)
    }
  }

  it should "return an updated shadow message with now() date, UC-1-2" in {
    // g i v e n
    val dto = CreateDto(11L, inputMessage_UC_1_2)

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Symbol("right")
    updatedShadow.map { json =>
      (json \ metadataKey).children.size shouldBe (shadowMessage_UC_1_2 \ metadataKey).children.size
      (json \ stateKey) shouldBe (shadowMessage_UC_1_1 \ stateKey)
    }
  }

}
