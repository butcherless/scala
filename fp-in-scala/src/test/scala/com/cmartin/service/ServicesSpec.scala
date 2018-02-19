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
    repo.getColorByNumber(1).isDefined must beTrue
  }

  "color with value 100 should not exist in the repository" >> {
    val repo = new ColorRepository
    repo.getColorByNumber(100).isDefined must beFalse
  }


  "service compositior" >> {
    val colorService = new ColorServiceImpl(new ColorRepository)
    val shapeService = new ShapeServiceImpl
    val fruitService = new FruitServiceImpl

    //TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val fruit = for {
      color <- colorService.getByHash(1)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(shape.id)
    } yield fruit

    fruit must beNone
  }
}
