package example

import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {
  "The WebServer object" should "say hello" in {
    WebServer.greeting shouldEqual "akka-http-hello"
  }
}
