package com.cmartin.learn.generator

import scala.util.{Failure, Success, Try}

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

object Main extends App {
  println("DtoGenerator algebra!")
  val typeRep = TryDtoGenerator.create(Type("mypkg", "MyTpe"))
  typeRep match {
    case Success(s) => println(s"TypeRepresentation[type: ${s.t}, file: ${s.f}]")
    case Failure(f) => println(s"function create type " + f.toString)
  }
  
}