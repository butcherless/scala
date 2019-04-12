package com.cmartin.learn

import java.util.UUID

import cats.{Id, ~>}
import com.cmartin.learn.algebra.{create, delete, read, update}
import com.cmartin.learn.interpreter.compiler

class IdCompilerSpecs extends AbstractCompilerSpecs {

  val idCompiler: algebra.CrudOperationA ~> Id = compiler

  it should "create a CryptoCurrency" in {
    val result: Id[String] = create(cryptoCurrency).map(c => c).foldMap(idCompiler)

    result shouldBe currencyName
  }

  it should "read a CryptoCurrency" in {
    val result: Id[CryptoCurrency] = read(currencyName).map(c => c).foldMap(idCompiler)

    result.name shouldBe currencyName
  }

  it should "update a CryptoCurrency" in {
    val result: Id[CryptoCurrency] = update(cryptoCurrency).map(c => c).foldMap(idCompiler)

    result shouldBe cryptoCurrency
  }

  it should "delete a CryptoCurrency" in {
    val result: Id[UUID] = delete(cryptoCurrency).map(c => c).foldMap(idCompiler)

    result shouldBe cryptoCurrency.id
  }
}
