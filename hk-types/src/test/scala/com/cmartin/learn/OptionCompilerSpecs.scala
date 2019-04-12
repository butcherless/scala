package com.cmartin.learn

import java.util.UUID

import cats.instances.option.catsStdInstancesForOption
import cats.~>
import com.cmartin.learn.algebra.{create, delete, read, update}
import com.cmartin.learn.interpreter.optionCompiler
import org.scalatest.OptionValues._

class OptionCompilerSpecs extends AbstractCompilerSpecs {

  val compiler: algebra.CrudOperationA ~> Option = optionCompiler

  it should "create a CryptoCurrency" in {
    val result: Option[String] = create(cryptoCurrency).map(c => c).foldMap(optionCompiler)

    result.value shouldBe currencyName
  }

  it should "read a CryptoCurrency" in {
    val result: Option[CryptoCurrency] = read(currencyName).map(c => c).foldMap(optionCompiler)

    result.value.name shouldBe currencyName
  }

  it should "update a CryptoCurrency" in {
    val result: Option[CryptoCurrency] = update(cryptoCurrency).map(c => c).foldMap(optionCompiler)

    result.value shouldBe cryptoCurrency
  }

  it should "delete a CryptoCurrency" in {
    val result: Option[UUID] = delete(cryptoCurrency).map(c => c).foldMap(optionCompiler)

    result.value shouldBe cryptoCurrency.id
  }
}
