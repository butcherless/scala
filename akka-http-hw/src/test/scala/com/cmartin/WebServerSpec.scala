package com.cmartin

import org.scalatest._

class WebServerSpec extends FlatSpec with Matchers {
  "The WebServer object" should "say hello" in {
    WebServer.greeting shouldEqual "akka-http-server"
  }
}
