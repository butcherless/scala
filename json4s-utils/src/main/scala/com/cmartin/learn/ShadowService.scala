package com.cmartin.learn

import com.cmartin.learn.Json4sResearch._
import org.json4s.JsonAST.JValue

class ShadowService(shadowRepository: ShadowRepository) {

  import ShadowService._

  def create(dto: CreateDto): Either[Throwable, JValue] = {
    val currentShadow: JValue = shadowRepository.findShadow(dto.id)
    val keys: List[String]    = getExclusionKeys(dto.entity \ metadataKey)

    for {
      timestamp       <- resolveTimestamp(dto.entity)
      filteredPayload <- filterPayload(dto.entity \ payloadKey, keys)
      shadowTimestamp <- getShadowTimestamp(currentShadow)
      shadow          <- buildShadow(timestamp, shadowTimestamp, filteredPayload, currentShadow)
      //_ <- shadowRepository.save(ShadowDbo(dto.id,JNothing))
    } yield shadow
  }

  private def buildShadow(data: (String, String, JValue, JValue)): Either[Throwable, JValue] = {
    Right {
      val (timestamp, shadowTimestamp, payload, currentShadow) = data
      if (isNewer(timestamp, shadowTimestamp)) {
        val metadata       = createMetadata(payload, timestamp)
        val incomingShadow = createShadow(payload, metadata)
        mergeShadows(currentShadow, incomingShadow)
      } else currentShadow
    }
  }

}

object ShadowService {

  def apply(shadowRepository: ShadowRepository): ShadowService =
    new ShadowService(shadowRepository)

  case class CreateDto(id: Long, entity: JValue)

  def log[T](t: T): Unit = println(s"debug: $t")

}
