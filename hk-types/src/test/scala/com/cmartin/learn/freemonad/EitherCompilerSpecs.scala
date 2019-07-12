package com.cmartin.learn.freemonad

import java.util.UUID

import cats.instances.either.catsStdInstancesForEither
import cats.~>
import com.cmartin.learn.freemonad.algebra.{create, delete, read, update}
import com.cmartin.learn.freemonad.freecats.SingleEither
import com.cmartin.learn.freemonad.interpreter.eitherCompiler

class EitherCompilerSpecs extends AbstractCompilerSpecs {

  val compiler: algebra.CrudOperationA ~> SingleEither = eitherCompiler

  "Either compiler" should "create a CryptoCurrency" in {
    val result: Either[String, String] = create(cryptoCurrency).map(c => c).foldMap(eitherCompiler)

    result shouldBe Right(currencyName)
  }

  it should "not create a CryptoCurrency, Left" in {
    val result: Either[String, String] =
      create(existingCryptoCurrency).map(c => c).foldMap(eitherCompiler)

    result shouldBe Left(constants.operationErrorMessage)
  }

  it should "read a CryptoCurrency" in {
    val result: Either[String, CryptoCurrency] =
      read(currencyName).map(c => c).foldMap(eitherCompiler)

    result.map(cc => cc.name) shouldBe Right(currencyName) //TODO fix uuid in read operation, Map repository
  }

  it should "not read a CryptoCurrency, Left" in {
    val result: Either[String, CryptoCurrency] =
      read(constants.notFoundName).map(c => c).foldMap(eitherCompiler)

    result shouldBe Left(constants.operationErrorMessage)
  }

  it should "update a CryptoCurrency" in {
    val result: Either[String, CryptoCurrency] =
      update(cryptoCurrency).map(c => c).foldMap(eitherCompiler)

    result shouldBe Right(cryptoCurrency)
  }

  it should "not update a CryptoCurrency, Left" in {
    val result: Either[String, CryptoCurrency] =
      update(nonExistingCryptoCurrency).map(c => c).foldMap(eitherCompiler)

    result shouldBe Left(constants.operationErrorMessage)
  }

  it should "delete a CryptoCurrency" in {
    val result: Either[String, UUID] = delete(cryptoCurrency).map(c => c).foldMap(eitherCompiler)

    result shouldBe Right(cryptoCurrency.id)
  }

  it should "not delete a CryptoCurrency, Left" in {
    val result: Either[String, UUID] =
      delete(nonExistingCryptoCurrency).map(c => c).foldMap(eitherCompiler)

    result shouldBe Left(constants.operationErrorMessage)
  }

}
