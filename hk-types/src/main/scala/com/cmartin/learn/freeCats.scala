package com.cmartin.learn

import cats.free.Free
import cats.free.Free.liftF
import cats.{Id, ~>}
import com.cmartin.learn.functions.buildUuid
// https://typelevel.org/cats/datatypes/freemonad.html
// https://blog.scalac.io/2016/06/02/overview-of-free-monad-in-cats.html

object freecats {

  // 1. Create an ADT Algebraic Data Type representing the business grammar

  /* Type A will be the type that Free Monad will be working on */
  sealed trait CrudOperationA[A]

  case class Create(cc: CrytoCurrency) extends CrudOperationA[String]

  case class Read(name: String) extends CrudOperationA[CrytoCurrency]

  case class Update() extends CrudOperationA[Unit]

  case class Delete() extends CrudOperationA[Unit]


  // 2. Free the ADT
  type CrudOperation[A] = Free[CrudOperationA, A] // give monadic feature to the ADT

  // Smart constructors
  def create(cc: CrytoCurrency): CrudOperation[String] = liftF(Create(cc))

  def read(name: String): CrudOperation[CrytoCurrency] = liftF(Read(name))

  def update(): CrudOperation[Unit] = liftF(Update())

  def delete(): CrudOperation[Unit] = liftF(Delete())


  // 3. Build a program made of a sequence of operations

  def myAwesomProgram(name: String): CrudOperation[CrytoCurrency] = for {
    cc <- read(name)
    nameLite <- create(cc.copy(name = s"${cc.name}Lite"))
    ccLite <- read(nameLite)
    _ <- update()
    //    _ <- create()
    _ <- delete()
  } yield ccLite


  // 4. Build the program compiler
  //val compiler: CrudOperationA ~> Id = ???


  def compiler: CrudOperationA ~> Id = new (CrudOperationA ~> Id) {
    def apply[A](fa: CrudOperationA[A]): Id[A] = fa match {
      case Create(cc) => println(s"create: $cc")
        cc.name
      case Read(name) => println(s"read: $name")
        buildCryptoCurrency(name)
      case Update() => println(s"update:")
        ()
      case Delete() => println(s"delete:")
        ()
    }
  }


  // H E L P E R
  def buildCryptoCurrency(name: String) =
    CrytoCurrency(buildUuid, name, BigDecimal(4933580502.0), BigDecimal(0.075038), 3.94)
}

object mainCats extends App {

  import com.cmartin.learn.freecats.{compiler, myAwesomProgram}

  println("Running my fucking interpreted & awesome program")
  val result = myAwesomProgram("BitCoin").foldMap(compiler)
}
