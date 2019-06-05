package com.cmartin.service

import com.cmartin.service.Services.nextFruitHash
import org.specs2.mutable.Specification

class ServicesSpec extends Specification {

  val RED           = "red"
  val LIGHT_RED     = "light-red"
  val COLOR_CODE_OK = 4
  val COLOR_CODE_KO = 2

  "hash function should generate an Int between 1 and 10 for Color class" >> {
    val color = Color("red", 1)
    val res   = Services.calcHashId(color)
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

  s"service compositior color -> $COLOR_CODE_OK should be Some" >> {
    val colorService: SimpleService[Color] = new ColorServiceImpl(new ColorRepository)
    val shapeService: SimpleService[Shape] = new ShapeServiceImpl(new ShapeRepository)
    val fruitService: SimpleService[Fruit] = new FruitServiceImpl(new FruitRepository)

    //TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val fruit = for {
      color <- colorService.getByHash(COLOR_CODE_OK)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(nextFruitHash(shape.name))
    } yield fruit

    fruit must beSome
  }

  s"service compositior color -> $COLOR_CODE_KO should be None" >> {
    val colorService: SimpleService[Color] = new ColorServiceImpl(new ColorRepository)
    val shapeService: SimpleService[Shape] = new ShapeServiceImpl(new ShapeRepository)
    val fruitService: SimpleService[Fruit] = new FruitServiceImpl(new FruitRepository)

    //TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val fruit = for {
      color <- colorService.getByHash(COLOR_CODE_KO)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(nextFruitHash(shape.name)) //TODO
    } yield fruit

    fruit must beNone
  }
}
