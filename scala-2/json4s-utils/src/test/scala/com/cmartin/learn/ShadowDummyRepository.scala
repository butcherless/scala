package com.cmartin.learn
import org.json4s.JValue
import org.json4s.JsonAST.JNothing

class ShadowDummyRepository extends ShadowRepository {
  override def findShadow(id: Long): JValue = {
    id match {
      case 0L  => JNothing
      case 11L => Json4sResearchTestUtils.shadowMessage_UC_1_1
      case 21L => Json4sResearchTestUtils.shadowMessage_UC_2_1
      case 31L => Json4sResearchTestUtils.shadowMessage_UC_3_1
      case 41L => Json4sResearchTestUtils.shadowMessage_UC_4_1
      case 42L => Json4sResearchTestUtils.shadowMessage_UC_4_2
      case _   => JNothing
    }
  }

  override def save(dbo: ShadowDbo): Long = 0L
}
object ShadowDummyRepository {
  def apply(): ShadowDummyRepository = new ShadowDummyRepository()
}
