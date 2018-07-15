package com.cmartin

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cmartin.route.ApiController
import org.scalatest._

class WebServerSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  val controller = new ApiController()

  "The WebServer object" should "say hello" in {
    WebServer.greeting shouldEqual "akka-http-server"
  }

  /* TODO
  "The controller GET hello" should "return hello message" in {
    Get(s"/${ControllerPath.HELLO}") ~> controller.route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`text/plain(UTF-8)`
      responseAs[String] shouldEqual HELLO_MESSAGE
    }
  }

  "The controller GET bye" should "return bye message" in {
    Get(s"/${ControllerPath.BYE}") ~> controller.route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`text/plain(UTF-8)`
      responseAs[String] shouldEqual BYE_MESSAGE
    }
  }
  */
}
