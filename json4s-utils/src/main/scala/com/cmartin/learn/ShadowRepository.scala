package com.cmartin.learn

import org.json4s.JValue

trait ShadowRepository {
  def findShadow(id: Long): JValue
  def save(dbo:ShadowDbo): Long
}

case class ShadowDbo(id:Long, data:JValue)