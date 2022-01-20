package com.cmartin.learn.freemonad

import cats.instances.all._
import com.cmartin.learn.freemonad.algebra._
import com.cmartin.learn.freemonad.interpreter._

import scala.concurrent.ExecutionContext.Implicits.global

object program {

  // 4. Build a program made of a sequence of operations

  def myAwesomeProgram(
      name: String,
      price: BigDecimal
  ): CrudOperation[CryptoCurrency] =
    for {
      cc       <- read(name)
      nameLite <- create(cc.copy(name = s"${cc.name}Lite"))
      ccLite   <- read(nameLite)
      _        <- update(cc.copy(id = cc.id, price = price))
      _        <- delete(cc)
    } yield ccLite

  object Application extends App {

    import scala.concurrent.Await
    import scala.concurrent.duration._

    // 6. Run the program, fold the sentence list
    println("\nRunning Id[A] program interpreter")
    val idResult =
      myAwesomeProgram("BitCoin", BigDecimal(0.077123)).foldMap(idCompiler)
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

}
