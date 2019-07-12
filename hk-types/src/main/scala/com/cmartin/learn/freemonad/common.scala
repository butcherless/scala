package com.cmartin.learn.freemonad

import java.util.UUID

import com.cmartin.learn.freemonad.functions._
import scalaz.syntax.apply._
import scalaz.syntax.validation._
import scalaz.{NonEmptyList, ValidationNel}

import scala.util.{Failure, Success, Try}

object constants {
  val notFoundUuid = buildUuid
  val foundUuid = buildUuid
  val notFoundName = "name-not-found"
  val operationErrorMessage = "operation-error-message"

}

object functions {
  def ZERO = BigDecimal(0)

  def buildUuid = UUID.randomUUID()

  def logErrorList(el: NonEmptyList[ValidationError]) = {
    el.foreach(println(_))
  }

  def printOption(o: Option[CryptoCurrency]) = o match {
    case Some(cc) => println(cc)
    case None => println("no currency found")
  }

  def printTry(t: Try[CryptoCurrency]) = t match {
    case Success(cc) => println(cc)
    case Failure(e) => println(e.getMessage)
  }

  // H E L P E R
  def buildCryptoCurrency(name: String) =
    CryptoCurrency(buildUuid, name, BigDecimal(4933580502.0), BigDecimal(0.075038), 3.94)

}

case class CryptoCurrency(
                           id: UUID,
                           name: String,
                           marketCap: BigDecimal,
                           price: BigDecimal,
                           change: Double = 0.0
                         )

object CryptoCurrency {
  implicit val ord = new Ordering[CryptoCurrency] {

    /**
      * Comparator for dependencies classes
      *
      * @param c1 one dependency
      * @param c2 another one dependency
      * @return 0 if equals, -1 if less than, +1 if greater than
      */
    def compare(c1: CryptoCurrency, c2: CryptoCurrency): Int = {
      c1.name.compareTo(c2.name)
    }
  }

  def checkName(name: String): ValidationNel[ValidationError, String] =
    if (name.isEmpty) ValidationError("Missing name").failureNel
    else name.success

  def checkNegativeValue(value: BigDecimal): ValidationNel[ValidationError, BigDecimal] =
    if (value <= ZERO) ValidationError(s"Negative value: ${value.toString}").failureNel
    else value.success

  def validate(
                name: String,
                marketCap: BigDecimal,
                price: BigDecimal
              ): ValidationNel[ValidationError, CryptoCurrency] =
    (checkName(name) |@|
      checkNegativeValue(marketCap) |@|
      checkNegativeValue(price)) { (name, cap, price) =>
      new CryptoCurrency(buildUuid, name, cap, price)
    }
}

object Factory {

  def newOptionCrytoCurrency(
                              name: String,
                              marketCap: BigDecimal,
                              price: BigDecimal
                            ): Option[CryptoCurrency] = {
    CryptoCurrency.validate(name, marketCap, price) match {
      case scalaz.Success(s) => Some(s)
      case scalaz.Failure(el) => {
        logErrorList(el)
        None
      }
    }
  }

  def newTryCrytoCurrency(
                           name: String,
                           marketCap: BigDecimal,
                           price: BigDecimal
                         ): Try[CryptoCurrency] = {
    // TODO validation NEL
    // TODO declare business exception
    if (name.isEmpty) Failure(new RuntimeException("empty name"))
    else if (marketCap <= ZERO) Failure(new RuntimeException("market cap less than zero"))
    else if (price <= ZERO) Failure(new RuntimeException("currency price less than zero"))
    else Try(CryptoCurrency(buildUuid, name, marketCap, price))
  }
}

class CrytoCurrencyRepository {

  import scala.collection.immutable.TreeSet

  val repo: TreeSet[CryptoCurrency] = TreeSet(
    CryptoCurrency(buildUuid, "Bitcoin", BigDecimal(153013899080.0), BigDecimal(8999.6), -0.84),
    CryptoCurrency(buildUuid, "Ethereum", BigDecimal(64306036497.0), BigDecimal(648.99), 0.65),
    CryptoCurrency(buildUuid, "Ripple", BigDecimal(32563377835.0), BigDecimal(0.831840), -0.84),
    CryptoCurrency(buildUuid, "Litecoin", BigDecimal(8292021839.0), BigDecimal(147.34), -2.02),
    CryptoCurrency(buildUuid, "TRON", BigDecimal(4933580502.0), BigDecimal(0.075038), 3.94)
  )

  def getByName(name: String) = repo.find(_.name == name)
}
