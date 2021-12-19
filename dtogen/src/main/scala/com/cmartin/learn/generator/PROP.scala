package com.cmartin.learn.generator

case class PROP(name: String, typ: Types) extends Builder {
  override def build: String = ???

  def withScope = this

  def withInitializer = this

  def isMandatory(toggle: Boolean) = this
}
