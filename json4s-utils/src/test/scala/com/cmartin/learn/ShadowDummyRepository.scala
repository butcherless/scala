package com.cmartin.learn
import org.json4s.JValue
import org.json4s.JsonAST.JNothing

class ShadowDummyRepository extends ShadowRepository {
  override def findShadow(id: Long): JValue = {
    id match {
      case 1L => JNothing
      case 2L => Json4sResearchTestUtils.shadowMessage_UC_2_1
      case _  => JNothing
    }
  }
}
object ShadowDummyRepository {
  def apply(): ShadowDummyRepository = new ShadowDummyRepository()
}
