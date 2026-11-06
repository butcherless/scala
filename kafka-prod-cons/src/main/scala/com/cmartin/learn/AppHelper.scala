package com.cmartin.learn
import com.cmartin.learn.Domain.DummyMessage
import io.circe.generic.auto.*
import io.circe.syntax.*

object AppHelper {
  def buildDummyMessage(dm: DummyMessage): String = dm.asJson.noSpaces
}
