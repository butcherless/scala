package com.cmartin.learn

import cats.free.Free
import cats.free.Free.liftF
import cats.{Id, ~>}
import com.cmartin.learn.functions.buildUuid

import scala.concurrent.Future

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

  def myAwesomeProgram(name: String): CrudOperation[CrytoCurrency] = for {
    cc <- read(name)
    nameLite <- create(cc.copy(name = s"${cc.name}Lite"))
    ccLite <- read(nameLite)
    _ <- update()
    //    _ <- create()
    _ <- delete()
  } yield ccLite


  // 4. Build the program compiler
  //val compiler: CrudOperationA ~> Id = ???
  //val compiler: CrudOperationA ~> Option = ???


  def compiler: CrudOperationA ~> Id = new (CrudOperationA ~> Id) {
    def apply[A](fa: CrudOperationA[A]): Id[A] = fa match {
      case Create(cc) => println(s"create id: $cc")
        cc.name
      case Read(name) => println(s"read id: $name")
        buildCryptoCurrency(name)
      case Update() => println(s"update id: TODO")
        ()
      case Delete() => println(s"delete id: TODO")
        ()
    }
  }

  def optionCompiler: CrudOperationA ~> Option = new (CrudOperationA ~> Option) {
    override def apply[A](fa: CrudOperationA[A]): Option[A] = fa match {
      case Create(cc) => println(s"create option: $cc")
        Some(cc.name)
      case Read(name) => println(s"read option: $name")
        Some(buildCryptoCurrency(name))
      case Update() => println(s"update option: TODO")
        Some(())
      case Delete() => println(s"delete option: TODO")
        Some(())
    }
  }

  type SingleEither[A] = Either[String, A]

  def eitherCompiler: CrudOperationA ~> SingleEither = new (CrudOperationA ~> SingleEither) {
    override def apply[A](fa: CrudOperationA[A]): Either[String, A] = fa match {
      case Create(cc) => println(s"create option: $cc")
        Right(cc.name)
      case Read(name) => println(s"read option: $name")
        Right(buildCryptoCurrency(name))
      case Update() => println(s"update option: TODO")
        Right(())
      case Delete() => println(s"delete option: TODO")
        Right(())
    }
  }

  def futureCompiler: CrudOperationA ~> Future = new (CrudOperationA ~> Future) {
    override def apply[A](fa: CrudOperationA[A]): Future[A] = fa match {
      case Create(cc) => println(s"create option: $cc")
        Future.successful(cc.name)
      case Read(name) => println(s"read option: $name")
        Future.successful(buildCryptoCurrency(name))
      case Update() => println(s"update option: TODO")
        Future.successful(())
      case Delete() => println(s"delete option: TODO")
        Future.successful(())
    }
  }


  // H E L P E R
  def buildCryptoCurrency(name: String) =
    CrytoCurrency(buildUuid, name, BigDecimal(4933580502.0), BigDecimal(0.075038), 3.94)
}

object mainCats extends App {

  import cats.instances.either.catsStdInstancesForEither
  import cats.instances.future.catsStdInstancesForFuture
  import cats.instances.option.catsStdInstancesForOption
  import com.cmartin.learn.freecats.{compiler, eitherCompiler, futureCompiler, myAwesomeProgram, optionCompiler}

  import scala.concurrent.ExecutionContext.Implicits.global

  println("\nRunning Id[A] program interpreter")
  val result = myAwesomeProgram("BitCoin").foldMap(compiler)

  println("\nRunning Option[A] program interpreter")
  val optionResult = myAwesomeProgram("LineCoin").foldMap(optionCompiler)

  println("\nRunning Either[String, A] program interpreter")
  val eitherResult = myAwesomeProgram("LineCoin").foldMap(eitherCompiler)

  println("\nRunning Future[A] program interpreter")
  val futureResult = myAwesomeProgram("LineCoin").foldMap(futureCompiler)
}
