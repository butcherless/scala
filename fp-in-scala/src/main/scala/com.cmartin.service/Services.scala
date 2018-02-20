package com.cmartin.service

import scala.collection.immutable.HashMap
import scala.util.Random

case class Color(name: String, next: Int)

case class Shape(id: Int, name: String, area: Double)

case class Fruit(id: Int, name: String, color: Color)

trait SimpleService[T] {
  def getByHash(h: Int): Option[T]
}

trait SimpleRepository[T] {
  def getById(id: Int): Option[T]

  def count(): Int

  def isEmpty(): Boolean
}

class ColorServiceImpl(repo: ColorRepository) extends SimpleService[Color] {
  override def getByHash(h: Int): Option[Color] = repo.getById(h)
}

//TODO
class ShapeServiceImpl(repo: SimpleRepository[Shape]) extends SimpleService[Shape] {
  override def getByHash(h: Int): Option[Shape] = repo.getById(h) // Some(Shape(1,"circle", math.Pi))
}

//TODO
class FruitServiceImpl extends SimpleService[Fruit] {
  override def getByHash(h: Int): Option[Fruit] = Some(Fruit(2, "apple", Color("yellow", 7)))
}

object Services {
  def calcHashId(t: Color): Int = math.abs(t.hashCode() % 10 + 1)
}

class ColorRepository extends SimpleRepository[Color] {
  private val colorNames = List("red", "yellow", "blue", "black", "white", "green", "grey", "pink", "brown", "cyan",
    "magenta")

  private var colorMap = HashMap[Int, Color]()

  for (i <- 1 to colorNames.length) {
    val color = Color(colorNames(i - 1), i * 2 - 1)
    colorMap += (i -> color)
  }

  override def getById(id: Int): Option[Color] = {
    colorMap.get(id)
  }

  override def count(): Int = colorMap.size

  override def isEmpty(): Boolean = colorMap.isEmpty

}

class ShapeRepository extends SimpleRepository[Shape] {
  private val shapeNames = List("circle", "cube", "cone", "triangle", "square", "oval", "rectangle", "cylinder",
    "pyramid", "sphere", "hexagon", "diamond", "star", "parallelogram", "pentagon", "octagon")

  private var shapeMap = HashMap[Int, Shape]()

  for (i <- 1 to shapeNames.length) {
    val shape = Shape(i, shapeNames(i - 1), 7 * Random.nextDouble() + 2)
    shapeMap += (3 * i - 1 -> shape)
  }

  override def getById(id: Int): Option[Shape] = {
    shapeMap.get(id)
  }

  override def count(): Int = shapeMap.size

  override def isEmpty(): Boolean = shapeMap.isEmpty
}

//TODO ShapeRepository & FruitRepository