package com.cmartin.service

import scala.collection.immutable.HashMap

case class Color(name: String)

case class Shape(id: Int, name: String, area: Double)

case class Fruit(id: Int, name: String, color: Color)

trait AbstractService[T] {
  def getByHash(h: Int): T
}

class ColorServiceImpl(repo: ColorRepository) extends AbstractService[Color] {

  override def getByHash(h: Int): Color = ???
}

class ShapeServiceImpl extends AbstractService[Shape] {
  override def getByHash(h: Int): Shape = ???
}

class FruitServiceImpl extends AbstractService[Fruit] {
  override def getByHash(h: Int): Fruit = ???
}

object Services {
  def calcHashId(t: Color): Int = math.abs(t.hashCode() % 10 + 1)

}

class ColorRepository {
  val colorNames = List("red", "yellow", "blue", "black", "white", "green", "grey", "pink", "brown", "cyan", "magenta")

  private var colorMap = HashMap[String, Color]()

  //  def createColors(): Unit = {
  for (name <- colorNames) {
    val color = Color(name)
    colorMap += (name -> color)
  }
  //  }

  def getColorByName(n: String): Option[Color] = {
    colorMap.get(n)
  }

  def colorCount(): Int = colorMap.size
}
