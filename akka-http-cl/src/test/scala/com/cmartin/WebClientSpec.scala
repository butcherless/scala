package com.cmartin

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WebClientSpec extends AnyFlatSpec with Matchers {
  "The WebClient object" should "say hello" in {
    WebClient.greeting shouldEqual "akka-http-client"
  }
}
