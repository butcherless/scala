package com.cmartin.learn
import  io.circe.generic.auto._
import io.circe.syntax._
import com.cmartin.learn.Domain.DummyMessage

object AppHelper {
  def buildDummyMessage(dm: DummyMessage): String = dm.asJson.noSpaces
}
