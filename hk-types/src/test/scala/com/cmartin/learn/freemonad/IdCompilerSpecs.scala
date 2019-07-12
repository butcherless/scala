package com.cmartin.learn.freemonad

import java.util.UUID

import cats.{Id, ~>}
import com.cmartin.learn.freemonad.algebra.{create, delete, read, update}
import com.cmartin.learn.freemonad.interpreter.idCompiler

class IdCompilerSpecs extends AbstractCompilerSpecs {

  val compiler: algebra.CrudOperationA ~> Id = idCompiler

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
