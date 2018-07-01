package com.cmartin

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cmartin.route.ApiController
import org.scalatest._

class WebServerSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  val controller = new ApiController()

  "The WebServer object" should "say hello" in {
    WebServer.greeting shouldEqual "akka-http-server"
  }

  "The controller" should "return hello" in {
    Get("/hello") ~> controller.route ~> check {
      responseAs[String] shouldEqual "hello from akka http"
    }
  }
}
