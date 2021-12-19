package com.cmartin.learn.freemonad

import java.util.UUID

import cats.free.Free
import cats.free.Free.liftF
import cats.implicits.catsStdInstancesForFuture
import cats.instances.all._
import cats.{Id, ~>}
import com.cmartin.learn.freemonad.functions.buildUuid

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
// https://typelevel.org/cats/datatypes/freemonad.html
// https://blog.scalac.io/2016/06/02/overview-of-free-monad-in-cats.html

object freecats {

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

  // 4. Build a program made of a sequence of operations

  def myAwesomeProgram(
      name: String,
      price: BigDecimal
  ): CrudOperation[CryptoCurrency] =
    for {
      cc <- read(name)
      nameLite <- create(cc.copy(name = s"${cc.name}Lite"))
      ccLite <- read(nameLite)
      _ <- update(cc.copy(id = cc.id, price = price))
      _ <- delete(cc)
    } yield ccLite

  // 5. Build the program compiler
  // val compiler: CrudOperationA ~> Id = ???
  // val compiler: CrudOperationA ~> Option = ???
  // val compiler: CrudOperationA ~> Either = ???
  // val compiler: CrudOperationA ~> Future = ???

  def compiler: CrudOperationA ~> Id =
    new (CrudOperationA ~> Id) {
      def apply[A](fa: CrudOperationA[A]): Id[A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Id: $cc")
            cc.name
          case Read(name) =>
            println(s"read name Id: $name")
            buildCryptoCurrency(name)
          case Update(cc) =>
            println(s"update crypto currency Id: ${cc}")
            cc
          case Delete(cc) =>
            println(s"delete crypto currency Id: ${cc.id}")
            cc.id
        }
    }

  def optionCompiler: CrudOperationA ~> Option =
    new (CrudOperationA ~> Option) {
      override def apply[A](fa: CrudOperationA[A]): Option[A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Option: $cc")
            Some(cc.name)
          case Read(name) =>
            println(s"read name Option: $name")
            Some(buildCryptoCurrency(name))
          // None
          case Update(cc) =>
            println(s"update crypto currency Option: ${cc}")
            Some(cc)
          case Delete(cc) =>
            println(s"delete crypto currency Option: ${cc.id}")
            Some(cc.id)
        }
    }

  type SingleEither[A] = Either[String, A]

  def eitherCompiler: CrudOperationA ~> SingleEither =
    new (CrudOperationA ~> SingleEither) {
      override def apply[A](fa: CrudOperationA[A]): Either[String, A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Either: $cc")
            Right(cc.name)
          case Read(name) =>
            println(s"read name Either: $name")
            // Right(buildCryptoCurrency(name))
            Left(s"unable to find: $name")
          case Update(cc) =>
            println(s"update crypto currency Either: ${cc}")
            Right(cc)
          case Delete(cc) =>
            println(s"delete crypto currency Either: ${cc.id}")
            Right(cc.id)
        }
    }

  def futureCompiler: CrudOperationA ~> Future =
    new (CrudOperationA ~> Future) {
      override def apply[A](fa: CrudOperationA[A]): Future[A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Future: ${cc}")
            Future.successful(cc.name)
          case Read(name) =>
            println(s"read name Future: $name")
            Future.successful(buildCryptoCurrency(name))
          case Update(cc) =>
            println(s"update crypto currency Future: ${cc}")
            Future.successful(cc)
          case Delete(cc) =>
            println(s"delete crypto currency Future: ${cc.id}")
            Future.successful(cc.id)
        }
    }

  // H E L P E R
  def buildCryptoCurrency(name: String) =
    CryptoCurrency(
      buildUuid,
      name,
      BigDecimal(4933580502.0),
      BigDecimal(0.075038),
      3.94
    )
}

object MainCats extends App {
  import freecats.{compiler, eitherCompiler, futureCompiler, myAwesomeProgram, optionCompiler}

  // 6. Run the program, fold the sentence list
  println("\nRunning Id[A] program interpreter")
  val idResult =
    myAwesomeProgram("BitCoin", BigDecimal(0.077123)).foldMap(compiler)
  println(s"Id Interpreter result: $idResult")

  println("\nRunning Option[A] program interpreter")
  val optionResult =
    myAwesomeProgram("LineCoin", BigDecimal(0.077123)).foldMap(optionCompiler)
  println(s"Option Interpreter result: $optionResult")

  println("\nRunning Either[String, A] program interpreter")
  val eitherResult =
    myAwesomeProgram("LineCoin", BigDecimal(0.077123)).foldMap(eitherCompiler)
  println(s"Either Interpreter result: $eitherResult")

  // TODO println("\nRunning Future[A] program interpreter")
  val futureResult =
    myAwesomeProgram("LineCoin", BigDecimal(0.077123)).foldMap(futureCompiler)

  Await.result(futureResult, 250 millis)
  println(s"Future Interpreter result: $futureResult")
}
