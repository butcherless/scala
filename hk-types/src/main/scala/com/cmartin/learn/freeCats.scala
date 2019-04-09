package com.cmartin.learn

import cats.free.Free
import cats.free.Free.liftF
import cats.{Id, ~>}
import com.cmartin.learn.functions.buildUuid

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

// https://typelevel.org/cats/datatypes/freemonad.html
// https://blog.scalac.io/2016/06/02/overview-of-free-monad-in-cats.html

object freecats {

  // 1. Create an ADT Algebraic Data Type representing the business grammar

  /* Type A will be the type that Free Monad will be working on */
  sealed trait CrudOperationA[A]

  case class Create(cc: CrytoCurrency) extends CrudOperationA[String]

  case class Read(name: String) extends CrudOperationA[CrytoCurrency]

  case class Update(cc: CrytoCurrency) extends CrudOperationA[CrytoCurrency]

  case class Delete() extends CrudOperationA[Unit]


  // 2. Free the ADT
  type CrudOperation[A] = Free[CrudOperationA, A] // give monadic feature to the ADT

  // 3. Smart constructors
  def create(cc: CrytoCurrency): CrudOperation[String] = liftF(Create(cc))

  def read(name: String): CrudOperation[CrytoCurrency] = liftF(Read(name))

  def update(cc: CrytoCurrency): CrudOperation[CrytoCurrency] = liftF(Update(cc))

  def delete(): CrudOperation[Unit] = liftF(Delete())


  // 4. Build a program made of a sequence of operations

  def myAwesomeProgram(name: String, price: BigDecimal): CrudOperation[CrytoCurrency] = for {
    cc <- read(name)
    nameLite <- create(cc.copy(name = s"${cc.name}Lite"))
    ccLite <- read(nameLite)
    ccUpdated <- update(cc.copy(price = price))
    _ <- delete()
  } yield ccLite


  // 5. Build the program compiler
  //val compiler: CrudOperationA ~> Id = ???
  //val compiler: CrudOperationA ~> Option = ???
  //val compiler: CrudOperationA ~> Either = ???
  //val compiler: CrudOperationA ~> Future = ???

  def compiler: CrudOperationA ~> Id = new (CrudOperationA ~> Id) {
    def apply[A](fa: CrudOperationA[A]): Id[A] = fa match {
      case Create(cc) => println(s"create crypto currency Id: $cc")
        cc.name
      case Read(name) => println(s"read name Id: $name")
        buildCryptoCurrency(name)
      case Update(cc) => println(s"update crypto currency Id: ${cc}")
        cc
      case Delete() => println(s"delete Id: TODO")
        ()
    }
  }

  def optionCompiler: CrudOperationA ~> Option = new (CrudOperationA ~> Option) {
    override def apply[A](fa: CrudOperationA[A]): Option[A] = fa match {
      case Create(cc) => println(s"create crypto currency Option: $cc")
        Some(cc.name)
      case Read(name) => println(s"read name Option: $name")
        Some(buildCryptoCurrency(name))
      case Update(cc) => println(s"update crypto currency Option: ${cc}")
        Some(cc)
      case Delete() => println(s"delete Option: TODO")
        Some(())
    }
  }

  type SingleEither[A] = Either[String, A]

  def eitherCompiler: CrudOperationA ~> SingleEither = new (CrudOperationA ~> SingleEither) {
    override def apply[A](fa: CrudOperationA[A]): Either[String, A] = fa match {
      case Create(cc) => println(s"create crypto currency Either: $cc")
        Right(cc.name)
      case Read(name) => println(s"read name Either: $name")
        Right(buildCryptoCurrency(name))
      case Update(cc) => println(s"update crypto currency Either: ${cc}")
        Right(cc)
      case Delete() => println(s"delete either: TODO")
        Right(())
    }
  }

  def futureCompiler: CrudOperationA ~> Future = new (CrudOperationA ~> Future) {
    override def apply[A](fa: CrudOperationA[A]): Future[A] = fa match {
      case Create(cc) => println(s"create crypto currency Future: ${cc}")
        Future.successful(cc.name)
      case Read(name) => println(s"read name Future: $name")
        Future.successful(buildCryptoCurrency(name))
      case Update(cc) => println(s"update crypto currency Future: ${cc}")
        Future.successful(cc)
      case Delete() => println(s"delete future: TODO")
        Future.successful(())
    }
  }


  // H E L P E R
  def buildCryptoCurrency(name: String) =
    CrytoCurrency(buildUuid, name, BigDecimal(4933580502.0), BigDecimal(0.075038), 3.94)
}

object MainCats extends App {

  import cats.instances.either.catsStdInstancesForEither
  import cats.instances.future.catsStdInstancesForFuture
  import cats.instances.option.catsStdInstancesForOption
  import com.cmartin.learn.freecats.{compiler, eitherCompiler, futureCompiler, myAwesomeProgram, optionCompiler}

  import scala.concurrent.ExecutionContext.Implicits.global

  // 6. Run the program, fold the sentence list
  println("\nRunning Id[A] program interpreter")
  val result = myAwesomeProgram("BitCoin", BigDecimal(0.077123)).foldMap(compiler)

  println("\nRunning Option[A] program interpreter")
  val optionResult = myAwesomeProgram("LineCoin", BigDecimal(0.077123)).foldMap(optionCompiler)

  println("\nRunning Either[String, A] program interpreter")
  val eitherResult = myAwesomeProgram("LineCoin", BigDecimal(0.077123)).foldMap(eitherCompiler)

  println("\nRunning Future[A] program interpreter")
  val futureResult = myAwesomeProgram("LineCoin", BigDecimal(0.077123)).foldMap(futureCompiler)

  Await.result(futureResult, 250 millis)
}
