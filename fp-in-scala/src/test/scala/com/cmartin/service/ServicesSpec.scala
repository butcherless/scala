package com.cmartin.service

import org.specs2.mutable.Specification

class ServicesSpec extends Specification {

  val RED = "red"
  val LIGHT_RED = "light-red"

  "hash function should generate an Int between 1 and 10 for Color class" >> {
    val color = Color("red", 1)
    val res = Services.calcHashId(color)
    res must beGreaterThanOrEqualTo(1) and beLessThanOrEqualTo(10)
  }

  "color repository should not be empty" >> {
    val repo = new ColorRepository
    repo.isEmpty() must beFalse
  }

  "color with value 1 should exist in the repository" >> {
    val repo = new ColorRepository
    repo.getById(1).isDefined must beTrue
  }

  "color with value 100 should not exist in the repository" >> {
    val repo = new ColorRepository
    repo.getById(100).isDefined must beFalse
  }

  "service compositior color -> 3 should be Some" >> {
    val colorService: SimpleService[Color] = new ColorServiceImpl(new ColorRepository)
    val shapeService: SimpleService[Shape] = new ShapeServiceImpl(new ShapeRepository)
    val fruitService: SimpleService[Fruit] = new FruitServiceImpl

    //TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val shape = for {
      color <- colorService.getByHash(3)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(shape.id) //TODO
    } yield shape
    println(s"result: $shape")
    shape must beSome
  }

  "service compositior color -> 1 should be None" >> {
    val colorService: SimpleService[Color] = new ColorServiceImpl(new ColorRepository)
    val shapeService: SimpleService[Shape] = new ShapeServiceImpl(new ShapeRepository)
    val fruitService: SimpleService[Fruit] = new FruitServiceImpl

    //TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val shape = for {
      color <- colorService.getByHash(1)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(shape.id) //TODO
    } yield shape
    println(s"result: $shape")
    shape must beNone
  }
}
