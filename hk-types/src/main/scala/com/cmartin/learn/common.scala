package com.cmartin.learn

import java.util.UUID

import com.cmartin.learn.functions._

object functions {
  def buildUuid = UUID.randomUUID()

}

object Factory {
  def ZERO = BigDecimal(0)


  def newCrytoCurrency(name: String, marketCap: BigDecimal, price: BigDecimal): Option[CrytoCurrency] = {
    // validation NEL
    if (name.isEmpty) None
    else if (marketCap <= ZERO) None
    else if (price <= ZERO) None
    else Some(CrytoCurrency(buildUuid, name, marketCap, price))
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