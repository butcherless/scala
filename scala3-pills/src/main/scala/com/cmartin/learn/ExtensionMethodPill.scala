package com.cmartin.learn

object ExtensionMethodPill {

  case class Country(code: Int, name: String)
  case class CountryView(code: String, name: String)

  extension(c: Country) {
    def toView: CountryView =
      CountryView(c.code.toString, c.name.toUpperCase)
  }

}
