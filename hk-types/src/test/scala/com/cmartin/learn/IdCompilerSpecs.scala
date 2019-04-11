package com.cmartin.learn

import cats.{Id, ~>}
import com.cmartin.learn.algebra.{CrudOperation, create, delete, read, update}
import com.cmartin.learn.interpreter.compiler
import org.scalatest.FlatSpec

class IdCompilerSpecs extends FlatSpec {

  val idCompiler: algebra.CrudOperationA ~> Id = compiler

  def program(name: String): CrudOperation[CryptoCurrency] = for {
    cc <- read(name)
  } yield cc

  it should "..." in {
    val result: Id[CryptoCurrency] = read("BitCoin").map(x => x).foldMap(idCompiler)

    result
  }
}
