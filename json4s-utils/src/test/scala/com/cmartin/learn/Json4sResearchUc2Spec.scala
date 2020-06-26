package com.cmartin.learn

import com.cmartin.learn.ShadowService.CreateDto
import org.json4s.JsonAST.JValue

class Json4sResearchUc2Spec extends Json4sResearchBaseSpec {
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 2"

  /*
   - inDto.payload
   - with @timestamp, dated and outdated
   - no t_field
   */

  it should "return a new shadow document with @timestamp date, UC-2-1" in {
    // g i v e n
    val dto                    = CreateDto(0L, inputMessage_UC_2_1)
    val expectedShadow: JValue = shadowMessage_UC_2_1

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

  it should "return untouched shadow document, outdated request, UC-2-2" in {
    // g i v e n
    val dto                    = CreateDto(21L, inputMessage_UC_2_2)
    val expectedShadow: JValue = shadowMessage_UC_2_1

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)

    /* TODO case missing date, JNothing
            createShadow operation , whole operation
            createShadowEntity (structure)
     */
  }

  it should "return an updated shadow document with @timestamp date, UC-2-3" in {
    // g i v e n
    val dto                    = CreateDto(21L, inputMessage_UC_2_3)
    val expectedShadow: JValue = shadowMessage_UC_2_3

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

}
