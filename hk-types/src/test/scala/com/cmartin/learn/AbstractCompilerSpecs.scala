package com.cmartin.learn

import com.cmartin.learn.functions.buildCryptoCurrency
import org.scalatest.{FlatSpec, Matchers}

abstract class AbstractCompilerSpecs extends FlatSpec with Matchers {
  val currencyName = "BitCoin"
  val cryptoCurrency = buildCryptoCurrency(currencyName)
}
