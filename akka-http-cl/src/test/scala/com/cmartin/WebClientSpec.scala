package com.cmartin

import org.scalatest._

class WebClientSpec extends FlatSpec with Matchers {
  "The WebClient object" should "say hello" in {
    WebClient.greeting shouldEqual "akka-http-client"
  }
}
