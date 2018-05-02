package com.cmartin.learn

import java.util.UUID

import com.cmartin.learn.functions._
import scalaz.NonEmptyList

import scala.util.{Failure, Success, Try}

object functions {
  def ZERO = BigDecimal(0)

  def buildUuid = UUID.randomUUID()

  def logErrorList(el: NonEmptyList[ValidationError]) = {
    el.foreach(println(_))
  }

  def printOption(o: Option[CrytoCurrency]) = o match {
    case Some(cc) => println(cc)
    case None => println("no currency found")
  }

  def printTry(t: Try[CrytoCurrency]) = t match {
    case Success(cc) => println(cc)
    case Failure(e) => println(e.getMessage)
  }
}

object Factory {

  def newOptionCrytoCurrency(name: String, marketCap: BigDecimal, price: BigDecimal): Option[CrytoCurrency] = {
    CrytoCurrency.validate(name, marketCap, price) match {
      case scalaz.Success(s) => Some(s)
      case scalaz.Failure(el) => {
        logErrorList(el)
        None
      }
    }
  }

  def newTryCrytoCurrency(name: String, marketCap: BigDecimal, price: BigDecimal): Try[CrytoCurrency] = {
    // TODO validation NEL
    // TODO declare business exception
    if (name.isEmpty) Failure(new RuntimeException("empty name"))
    else if (marketCap <= ZERO) Failure(new RuntimeException("market cap less than zero"))
    else if (price <= ZERO) Failure(new RuntimeException("currency price less than zero"))
    else Try(CrytoCurrency(buildUuid, name, marketCap, price))
  }
}

class CrytoCurrencyRepository {

  import scala.collection.immutable.TreeSet

  val repo: TreeSet[CrytoCurrency] = TreeSet(
    CrytoCurrency(buildUuid, "Bitcoin", BigDecimal(153013899080.0), BigDecimal(8999.6), -0.84),
    CrytoCurrency(buildUuid, "Ethereum", BigDecimal(64306036497.0), BigDecimal(648.99), 0.65),
    CrytoCurrency(buildUuid, "Ripple", BigDecimal(32563377835.0), BigDecimal(0.831840), -0.84),
    CrytoCurrency(buildUuid, "Litecoin", BigDecimal(8292021839.0), BigDecimal(147.34), -2.02),
    CrytoCurrency(buildUuid, "TRON", BigDecimal(4933580502.0), BigDecimal(0.075038), 3.94)
  )

  def getByName(name: String) = repo.find(_.name == name)
}