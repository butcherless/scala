package com.cmartin.learn
import com.cmartin.learn.Domain.DummyMessage
import io.circe.generic.auto._
import io.circe.syntax._

object AppHelper {
  def buildDummyMessage(dm: DummyMessage): String = dm.asJson.noSpaces
}
