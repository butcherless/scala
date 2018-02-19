package com.cmartin.service

import scala.collection.immutable.HashMap

case class Color(name: String, next: Int)

case class Shape(id: Int, name: String, area: Double)

case class Fruit(id: Int, name: String, color: Color)

trait AbstractService[T] {
  def getByHash(h: Int): Option[T]
}

class ColorServiceImpl(repo: ColorRepository) extends AbstractService[Color] {
  override def getByHash(h: Int): Option[Color] = repo.getColorByNumber(h)
}

//TODO
class ShapeServiceImpl extends AbstractService[Shape] {
  override def getByHash(h: Int): Option[Shape] = None // Some(Shape(1,"circle", math.Pi))
}

//TODO
class FruitServiceImpl extends AbstractService[Fruit] {
  override def getByHash(h: Int): Option[Fruit] = Some(Fruit(2, "apple", Color("yellow", 7)))
}

object Services {
  def calcHashId(t: Color): Int = math.abs(t.hashCode() % 10 + 1)
}

class ColorRepository {
  val colorNames = List("red", "yellow", "blue", "black", "white", "green", "grey", "pink", "brown", "cyan", "magenta")

  private var colorMap = HashMap[Int, Color]()

  for (i <- 1 to colorNames.length) {
    val color = Color(colorNames(i - 1), i * 2 - 1)
    colorMap += (i -> color)
  }

  def getColorByNumber(n: Int): Option[Color] = {
    colorMap.get(n)
  }

  def colorCount(): Int = colorMap.size

  def isEmpty(): Boolean = colorNames.isEmpty

}

//TODO ShapeRepository & FruitRepository