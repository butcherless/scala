package com.cmartin.learn.freemonad

import java.util.UUID

import cats.~>
import com.cmartin.learn.freemonad.algebra.{create, delete, read, update}
import com.cmartin.learn.freemonad.interpreter.futureCompiler
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FutureCompilerSpecs extends AbstractCompilerSpecs with ScalaFutures { //TODO change to AsyncFlatSpec

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds))

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
