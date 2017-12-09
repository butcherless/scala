package com.cmartin.learn.generator

import scala.util.Try

trait DtoGenerator[P[_]] {
  def create(t: Type): P[TypeRepresentation]

  /*
  def addProperty(prop: Property): P[TypeRepresentation]

  def addGetter(prop: Property): P[TypeRepresentation]

  def addToString(builder: ToStringBuilder): P[TypeRepresentation]
  */
}

object TryDtoGenerator extends DtoGenerator[Try] {
  override def create(t: Type) = {
    Try(TypeRepresentation(t, File("dir", "name")))
  }

  /*
  override def addProperty(prop: Property) = ???

  override def addGetter(prop: Property) = ???

  override def addToString(builder: ToStringBuilder) = ???
  */

}

object OptionDtoGenerator extends DtoGenerator[Option] {
  override def create(t: Type) = {
    val tr = TypeRepresentation(t, File("", t.name))
    tr.copy(t.copy(pkg = "dummy"))
    Option(TypeRepresentation(t, File("dir", "name")))
  }

  /*
  override def addProperty(prop: Property) = ???

  override def addGetter(prop: Property) = ???

  override def addToString(builder: ToStringBuilder) = ???
  */
}