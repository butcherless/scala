package com.cmartin.learn.freemonad

import com.cmartin.learn.freemonad.functions.buildCryptoCurrency
import org.scalatest.{FlatSpec, Matchers}

abstract class AbstractCompilerSpecs extends FlatSpec with Matchers {
  val currencyName = "BitCoin"
  val cryptoCurrency = buildCryptoCurrency(currencyName)
  val existingCryptoCurrency = cryptoCurrency.copy(id = constants.foundUuid)
  val nonExistingCryptoCurrency = cryptoCurrency.copy(id = constants.notFoundUuid)
}
