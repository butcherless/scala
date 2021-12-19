package com.cmartin.learn.generator

case class DTO(name: String) extends Builder {
  override def build: String = "dummy string"

  def withGetters = this

  def withProperties(props: List[PROP]) = this

  def withProperty(property: PROP) = this

  def withSerializable = this

  def withToString = this
}
