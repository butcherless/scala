package com.cmartin.learn

import com.cmartin.learn.ShadowService.CreateDto
import org.json4s.JsonAST.JValue
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Json4sResearchUc2Spec extends AnyFlatSpec with Matchers {
  import Json4sResearchTestUtils._

  behavior of "Json4sResearch Use Case 2"

  val shadowRepository: ShadowRepository = ShadowDummyRepository()
  val shadowService: ShadowService       = ShadowService(shadowRepository)

  /*
   - with @timestamp
   - no t_field
   */

  it should "return a new shadow document with @timestamp date, UC-2-1" in {
    // g i v e n
    val dto                    = CreateDto(1L, inputMessage_UC_2_1)
    val expectedShadow: JValue = shadowMessage_UC_2_1

    // w h e n
    val updatedShadow: Either[Throwable, JValue] = shadowService.create(dto)

    // t h e n
    updatedShadow shouldBe Right(expectedShadow)
  }

  it should "TODO return untouched shadow document, outdated request, UC-2-2" in {
    // g i v e n
    val dto                    = CreateDto(2L, inputMessage_UC_2_2)
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

}
