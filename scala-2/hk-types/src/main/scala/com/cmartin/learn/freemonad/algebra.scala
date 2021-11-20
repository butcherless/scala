package com.cmartin.learn.freemonad

import java.util.UUID

import cats.free.Free
import cats.free.Free.liftF

object algebra {

  // 1. Create an ADT Algebraic Data Type representing the business grammar

  /* Type A will be the type that Free Monad will be working on */
  sealed trait CrudOperationA[A]

  case class Create(cc: CryptoCurrency) extends CrudOperationA[String]

  case class Read(name: String) extends CrudOperationA[CryptoCurrency]

  case class Update(cc: CryptoCurrency) extends CrudOperationA[CryptoCurrency]

  case class Delete(cc: CryptoCurrency) extends CrudOperationA[UUID]

  // 2. Free the ADT
  type CrudOperation[A] =
    Free[CrudOperationA, A] // give monadic feature to the ADT

  // 3. Smart constructors
  def create(cc: CryptoCurrency): CrudOperation[String] = liftF(Create(cc))

  def read(name: String): CrudOperation[CryptoCurrency] = liftF(Read(name))

  def update(cc: CryptoCurrency): CrudOperation[CryptoCurrency] = liftF(
    Update(cc)
  )

  def delete(cc: CryptoCurrency): CrudOperation[UUID] = liftF(Delete(cc))

}
