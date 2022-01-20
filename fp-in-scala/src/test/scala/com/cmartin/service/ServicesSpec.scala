package com.cmartin.service
import com.cmartin.service.Services.nextFruitHash
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ServicesSpec extends AnyFlatSpec with Matchers {

  val RED           = "red"
  val LIGHT_RED     = "light-red"
  val COLOR_CODE_OK = 4
  val COLOR_CODE_KO = 2

  it should "hash function should generate an Int between 1 and 10 for Color class" in {
    val color = Color("red", 1)
    val res   = Services.calcHashId(color)
    res shouldBe >(1)
    res shouldBe <=(10)
  }

  it should "color repository should not be empty" in {
    val repo = new ColorRepository
    repo.isEmpty() shouldBe false
  }

  it should "color with value 1 should exist in the repository" in {
    val repo = new ColorRepository
    repo.getById(1).isDefined shouldBe true
  }

  it should "color with value 100 should not exist in the repository" in {
    val repo = new ColorRepository
    repo.getById(100).isDefined shouldBe false
  }

  it should s"service compositior color -> $COLOR_CODE_OK should be Some" in {
    val colorService: SimpleService[Color] =
      new ColorServiceImpl(new ColorRepository)
    val shapeService: SimpleService[Shape] =
      new ShapeServiceImpl(new ShapeRepository)
    val fruitService: SimpleService[Fruit] =
      new FruitServiceImpl(new FruitRepository)

    // TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val fruit = for {
      color <- colorService.getByHash(COLOR_CODE_OK)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(nextFruitHash(shape.name))
    } yield fruit

    fruit.isDefined shouldBe true
  }

  it should s"service compositior color -> $COLOR_CODE_KO should be None" in {
    val colorService: SimpleService[Color] =
      new ColorServiceImpl(new ColorRepository)
    val shapeService: SimpleService[Shape] =
      new ShapeServiceImpl(new ShapeRepository)
    val fruitService: SimpleService[Fruit] =
      new FruitServiceImpl(new FruitRepository)

    // TODO refactor for-comprehension in a funtion f(x: Int) : Option[Fruit)

    val fruit = for {
      color <- colorService.getByHash(COLOR_CODE_KO)
      shape <- shapeService.getByHash(color.next)
      fruit <- fruitService.getByHash(nextFruitHash(shape.name)) // TODO
    } yield fruit

    fruit shouldBe None
  }
}
