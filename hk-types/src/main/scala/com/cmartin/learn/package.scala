package com.cmartin.learn

import cats.free.Free
import cats.free.Free.liftF
import cats.{Id, ~>}

// https://typelevel.org/cats/datatypes/freemonad.html
package object freecats {

  // 1. Create an ADT Algebraic Data Type representing the business grammar

  sealed trait CrudFacadeA[A]

  case class Create(cc: CrytoCurrency) extends CrudFacadeA[Unit]

  case class Read() extends CrudFacadeA[Unit]

  case class Update() extends CrudFacadeA[Unit]

  case class Delete() extends CrudFacadeA[Unit]


  // 2. Free the ADT
  type CrudFacade[A] = Free[CrudFacadeA, A] // give monadic feature to the ADT

  // Smart constructors
  def create(cc: CrytoCurrency): CrudFacade[Unit] = liftF(Create(cc))

  def read(): CrudFacade[Unit] = liftF(Read())

  def update(): CrudFacade[Unit] = liftF(Update())

  def delete(): CrudFacade[Unit] = liftF(Delete())


  // 3. Build a program

  def myAwesomProgram(cc: CrytoCurrency): CrudFacade[Unit] = for {
    _ <- read()
    _ <- create(cc)
    _ <- read()
    _ <- update()
    //    _ <- create()
    _ <- delete()
  } yield ()


  // 4. Build the program compiler
  def compiler: CrudFacadeA ~> Id = ???

  /*
  def compiler: CrudFacadeA ~> Id = new (CrudFacadeA ~> Id) {
    def apply[A](fa: CrudFacadeA[A]): Id[A] = fa match {
      case Read() => println(s"create function")
        ()
    }
  }
  */
}
