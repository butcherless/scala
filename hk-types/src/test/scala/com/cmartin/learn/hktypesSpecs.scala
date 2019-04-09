package com.cmartin.learn

import com.cmartin.learn.functions._
import org.scalatest.OptionValues._
import org.scalatest._


class hktypesSpecs extends FeatureSpec with GivenWhenThen {

  val BITCOIN_NAME = "Bitcoin"

  val service = new OptionCoinMarketService(new CrytoCurrencyRepository())

  info("Coin Martket Cap Service Tests")

  feature("Create cryto currency") {

    scenario("create crypto coin success") {

      Given("coin market service and valid crypto currency")
      val cc = CryptoCurrency(buildUuid, BITCOIN_NAME, BigDecimal(5000), BigDecimal(10))

      When("call create operation")
      val result: Option[CryptoCurrency] = service.create(cc)

      Then("service returns a valid crypto currency")
      val currency = result.value
      assert(currency.id.toString.length == 36)
      assert(currency.name == cc.name)
      assert(currency.marketCap == cc.marketCap)
      assert(currency.price == cc.price)
      assert(currency.change == cc.change)
    }


    scenario("create crypto coin failure") {

      Given("coin market service and invalid crypto currency")
      val cc = CryptoCurrency(buildUuid, "", BigDecimal(10), BigDecimal(10))

      When("call create operation")
      val result = service.create(cc)

      Then("serivce returns empty currency")
      assert(result.isEmpty)
    }
  }

  feature("Read cryto currency") {
    scenario("read crypto coin by name success") {
      Given("coin market service and an existing crypto currency name")

      When("call read by name operation")
      val result = service.readByName(BITCOIN_NAME)

      Then("service return the currency")
      assert(result.isDefined)
    }
  }
}
