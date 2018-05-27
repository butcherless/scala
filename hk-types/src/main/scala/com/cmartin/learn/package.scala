package com.cmartin.learn

import cats.free.Free
import cats.free.Free.liftF
import cats.~>

// https://typelevel.org/cats/datatypes/freemonad.html
// https://blog.scalac.io/2016/06/02/overview-of-free-monad-in-cats.html

package object freecats {

  // 1. Create an ADT Algebraic Data Type representing the business grammar

  sealed trait CrudFacadeA[A]

  case class Create(cc: CrytoCurrency) extends CrudFacadeA[String]

  case class Read(name: String) extends CrudFacadeA[CrytoCurrency]

  case class Update() extends CrudFacadeA[Unit]

  case class Delete() extends CrudFacadeA[Unit]


  // 2. Free the ADT
  type CrudFacade[A] = Free[CrudFacadeA, A] // give monadic feature to the ADT

  // Smart constructors
  def create(cc: CrytoCurrency): CrudFacade[String] = liftF(Create(cc))

  def read(name: String): CrudFacade[CrytoCurrency] = liftF(Read(name))

  def update(): CrudFacade[Unit] = liftF(Update())

  def delete(): CrudFacade[Unit] = liftF(Delete())


  // 3. Build a program

  def myAwesomProgram(name: String) = for {
    cc <- read(name)
    nameLite <- create(cc.copy(name = s"${cc.name}Lite"))
    ccLite <- read(nameLite)
    _ <- update()
    //    _ <- create()
    _ <- delete()
  } yield ccLite


  // 4. Build the program compiler
  //val compiler: CrudFacadeA ~> Id = ???

/*
  val compiler: CrudFacadeA ~> CrudFacade = new (CrudFacadeA ~> CrudFacade) {
    def apply[A](fa: CrudFacadeA[A]): CrudFacade[A] = fa match {
      case Read(name) => read(name)
      //println(s"create function")

    }
  }
*/
}
