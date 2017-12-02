package com.cmartin.learn.generator

import scala.util.Try

trait DtoGenerator[P[_]] {
  def create(name: Type): P[TypeRepresentation]

  def addProperty(prop: Property, t: Type): P[TypeRepresentation]

  def addGetters(t: Type): P[TypeRepresentation]

  def addToString(builder: ToStringBuilder, t: Type): P[TypeRepresentation]
}

object TryDtoGenerator extends DtoGenerator[Try] {
  override def create(name: Type) = ???

  override def addProperty(prop: Property, t: Type) = ???

  override def addGetters(t: Type) = ???

  override def addToString(builder: ToStringBuilder, t: Type) = ???
}