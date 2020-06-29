package com.cmartin.learn

import com.cmartin.learn.ShadowService.CreateDto
import org.json4s.JsonAST.JValue

class Json4sResearchUc3Spec extends Json4sResearchBaseSpec {
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 3"

  /*
   - inDto.t_field
   - with t_field, cts_ts
   - with @timestamp
   */

  it should "return a new shadow document with t_field date, UC-3-1" in {
    // g i v e n
    val dto                    = CreateDto(0L, inputMessage_UC_3_1)
    val expectedShadow: JValue = shadowMessage_UC_3_1

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

  it should "return an updated shadow document with @timestamp date, UC-3-2" in {
    // g i v e n , 31L retrieves current state shadowMessage_UC_3_1
    val dto                    = CreateDto(31L, inputMessage_UC_3_2)
    val expectedShadow: JValue = shadowMessage_UC_3_2

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)

  }
}
