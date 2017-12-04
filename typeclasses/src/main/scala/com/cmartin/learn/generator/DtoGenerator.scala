package com.cmartin.learn.generator

import scala.util.Try

trait DtoGenerator[P[_]] {
  def create(name: Type): P[TypeRepresentation]

  def addProperty(prop: Property): P[TypeRepresentation]

  def addGetters(t: Type): P[TypeRepresentation]

  def addToString(builder: ToStringBuilder): P[TypeRepresentation]
}

object TryDtoGenerator extends DtoGenerator[Try] {
  override def create(name: Type) = {
    Try(TypeRepresentation(Type("pkg", "name"), File("dir", "name")))
  }

  override def addProperty(prop: Property) = ???

  override def addGetters(t: Type) = ???

  override def addToString(builder: ToStringBuilder) = ???
}

object OptionDtoGenerator extends DtoGenerator[Option] {
  override def create(name: Type) = {
    Option(TypeRepresentation(Type("pkg", "name"), File("dir", "name")))
  }

  override def addProperty(prop: Property) = ???

  override def addGetters(t: Type) = ???

  override def addToString(builder: ToStringBuilder) = ???
}