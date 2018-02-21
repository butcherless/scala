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
  override def getByHash(h: Int): Option[Color] = {
    val color = repo.getById(h)
    println(color)
    color
  }
}

class ShapeServiceImpl(repo: SimpleRepository[Shape]) extends SimpleService[Shape] {
  override def getByHash(h: Int): Option[Shape] = {
    val shape = repo.getById(h)
    println(shape)
    shape
  }
}

class FruitServiceImpl(repo: SimpleRepository[Fruit]) extends SimpleService[Fruit] {
  override def getByHash(h: Int): Option[Fruit] = {
    val fruit = repo.getById(h)
    println(fruit)
    fruit
  }
}

object Services {
  def calcHashId(t: Color): Int = math.abs(t.hashCode() % 10 + 1)

  def getEven(i: Int): Int = 2 * i

  def getOdd(i: Int): Int = getEven(i) - 1

  def nextFruitHash(s: String): Int = s.length - math.abs(s.hashCode) % 2

  /**
    * generates sequence: 1, 4, 7, 10, 13
    *
    * @param i
    * @return
    */
  def tripleMinusTwo(i: Int): Int = 3 * i - 2
}

class ColorRepository extends SimpleRepository[Color] {

  import Services.getOdd

  private val colorNames =
    List("red", "yellow", "blue", "black", "white", "green", "grey", "pink", "brown", "cyan", "magenta")

  private var colorMap = HashMap[Int, Color]()

  // odd numbers
  for (i <- 1 to colorNames.length) {
    val color = Color(colorNames(i - 1), getOdd(i))
    colorMap += (i -> color)
  }

  override def getById(id: Int): Option[Color] = colorMap.get(id)

  override def count(): Int = colorMap.size

  override def isEmpty(): Boolean = colorMap.isEmpty

}

class ShapeRepository extends SimpleRepository[Shape] {

  import Services.tripleMinusTwo

  private val shapeNames = List("circle", "cube", "cone", "triangle", "square", "oval", "rectangle", "cylinder",
    "pyramid", "sphere", "hexagon", "diamond", "star", "parallelogram", "pentagon", "octagon")

  private var shapeMap = HashMap[Int, Shape]()

  for (i <- 1 to shapeNames.length) {
    val shape = Shape(i, shapeNames(i - 1), 7 * Random.nextDouble() + 2)
    shapeMap += (tripleMinusTwo(i) -> shape)
  }

  override def getById(id: Int): Option[Shape] = shapeMap.get(id)

  override def count(): Int = shapeMap.size

  override def isEmpty(): Boolean = shapeMap.isEmpty
}

class FruitRepository extends SimpleRepository[Fruit] {

  import Services.getOdd

  private val fruitNames = List("apple", "banana", "orange", "mandarin", "grapefruit", "lemon", "lime", "cherry",
    "strawberry", "peach", "coconut")

  private var fruitMap = HashMap[Int, Fruit]()

  for (i <- 1 to fruitNames.length) {
    val shape = Fruit(i, fruitNames(i - 1), Color("undefined", 0))
    fruitMap += (getOdd(i) -> shape)
  }

  override def getById(id: Int): Option[Fruit] = fruitMap.get(id)

  override def count(): Int = fruitMap.size

  override def isEmpty(): Boolean = fruitMap.isEmpty
}
