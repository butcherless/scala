package com.cmartin.learn

import java.util.UUID

import cats.instances.future.catsStdInstancesForFuture
import cats.~>
import com.cmartin.learn.algebra.{create, delete, read, update}
import com.cmartin.learn.interpreter.futureCompiler
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FutureCompilerSpecs extends AbstractCompilerSpecs with ScalaFutures {

  val compiler: algebra.CrudOperationA ~> Future = futureCompiler

  it should "create a CryptoCurrency" in {

    val result: Future[String] = create(cryptoCurrency).map(c => c).foldMap(futureCompiler)

    whenReady(result) { name =>
      name shouldBe currencyName
    }
  }

  it should "not create a CryptoCurrency, Failure" in {

    val result: Future[String] = create(existingCryptoCurrency).map(c => c).foldMap(futureCompiler)

    whenReady(result.failed) { e =>
      e.getMessage shouldBe constants.operationErrorMessage
    }
  }

  it should "read a CryptoCurrency" in {
    val result: Future[CryptoCurrency] = read(currencyName).map(c => c).foldMap(futureCompiler)

    whenReady(result) { cc =>
      cc.name shouldBe currencyName
    }
  }

  it should "not read a CryptoCurrency, Failure" in {
    val result: Future[CryptoCurrency] =
      read(constants.notFoundName).map(c => c).foldMap(futureCompiler)

    whenReady(result.failed) { e =>
      e.getMessage shouldBe constants.operationErrorMessage
    }
  }

  it should "update a CryptoCurrency" in {
    val result: Future[CryptoCurrency] = update(cryptoCurrency).map(c => c).foldMap(futureCompiler)

    whenReady(result) { cc =>
      cc shouldBe cryptoCurrency
    }
  }

  it should "not update a CryptoCurrency, Failure" in {
    val result: Future[CryptoCurrency] =
      update(nonExistingCryptoCurrency).map(c => c).foldMap(futureCompiler)

    whenReady(result.failed) { e =>
      e.getMessage shouldBe constants.operationErrorMessage
    }
  }

  it should "delete a CryptoCurrency" in {
    val result: Future[UUID] = delete(cryptoCurrency).map(c => c).foldMap(futureCompiler)

    whenReady(result) { cc =>
      cc shouldBe cryptoCurrency.id
    }
  }

  it should "not delete a CryptoCurrency, Failure" in {
    val result: Future[UUID] = delete(nonExistingCryptoCurrency).map(c => c).foldMap(futureCompiler)

    whenReady(result.failed) { e =>
      e.getMessage shouldBe constants.operationErrorMessage
    }
  }
}
