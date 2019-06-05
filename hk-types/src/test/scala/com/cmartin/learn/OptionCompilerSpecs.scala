package com.cmartin.learn

import java.util.UUID

import cats.instances.option.catsStdInstancesForOption
import cats.~>
import com.cmartin.learn.algebra.{ create, delete, read, update }
import com.cmartin.learn.interpreter.optionCompiler
import org.scalatest.OptionValues._

class OptionCompilerSpecs extends AbstractCompilerSpecs {

  val compiler: algebra.CrudOperationA ~> Option = optionCompiler

  it should "create a CryptoCurrency, Some" in {
    val result: Option[String] = create(cryptoCurrency).map(c => c).foldMap(optionCompiler)

    result shouldBe Some(currencyName)
  }

  it should "not create a CryptoCurrency, None" in {
    val result: Option[String] = create(existingCryptoCurrency).map(c => c).foldMap(optionCompiler)

    result shouldBe None
  }

  it should "read a CryptoCurrency, Some" in {
    val result: Option[CryptoCurrency] = read(currencyName).map(c => c).foldMap(optionCompiler)

    result.value.name shouldBe currencyName
  }

  it should "not read a CryptoCurrency, None" in {
    val result: Option[CryptoCurrency] = read(constants.notFoundName).map(c => c).foldMap(optionCompiler)

    result shouldBe None
  }

  it should "update a CryptoCurrency, Some" in {
    val result: Option[CryptoCurrency] = update(cryptoCurrency).map(c => c).foldMap(optionCompiler)

    result.value shouldBe cryptoCurrency
  }

  it should "not update a CryptoCurrency, None" in {
    val result: Option[CryptoCurrency] = update(nonExistingCryptoCurrency).map(c => c).foldMap(optionCompiler)

    result shouldBe None
  }

  it should "delete a CryptoCurrency, Some" in {
    val result: Option[UUID] = delete(cryptoCurrency).map(c => c).foldMap(optionCompiler)

    result.value shouldBe cryptoCurrency.id
  }

  it should "not delete a CryptoCurrency, None" in {
    val result: Option[UUID] = delete(nonExistingCryptoCurrency).map(c => c).foldMap(optionCompiler)

    result shouldBe None
  }
}
