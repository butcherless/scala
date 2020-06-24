package com.cmartin.learn

import org.json4s.JValue

trait ShadowRepository {
  def findShadow(id: Long): JValue
}
