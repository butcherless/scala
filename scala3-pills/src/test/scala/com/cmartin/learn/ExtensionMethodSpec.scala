package com.cmartin.learn
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ExtensionMethodPill.*

class ExtensionMethodSpec extends AnyFlatSpec with Matchers {

  behavior of "ExtensionMethod"

  it should "convert a domain object to a view object" in {
    val country = Country(34, "Spain")

    val result = country.toView

    result shouldBe CountryView("34", "SPAIN")
  }
}
