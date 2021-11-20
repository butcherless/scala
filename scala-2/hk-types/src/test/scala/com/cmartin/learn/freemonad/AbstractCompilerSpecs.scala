package com.cmartin.learn.freemonad

import com.cmartin.learn.freemonad.functions.buildCryptoCurrency
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class AbstractCompilerSpecs extends AnyFlatSpec with Matchers {
  val currencyName = "BitCoin"
  val cryptoCurrency = buildCryptoCurrency(currencyName)
  val existingCryptoCurrency = cryptoCurrency.copy(id = constants.foundUuid)
  val nonExistingCryptoCurrency =
    cryptoCurrency.copy(id = constants.notFoundUuid)
}
