package com.cmartin

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cmartin.route.{ApiController, ControllerPath, JsonSupport}
import org.scalatest._

class WebServerSpec extends FlatSpec with Matchers with ScalatestRouteTest with JsonSupport {
  val controller = new ApiController()

  "The WebServer object" should "say hello" in {
    WebServer.greeting shouldEqual "akka-http-server"
  }

  "The controller GET /hello" should "return hello message" in {
    Get(s"/${ControllerPath.HELLO}") ~> controller.route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[String].contains(route.HELLO_MESSAGE) shouldBe true
    }
  }

  "The controller GET /bye" should "return bye message" in {
    Get(s"/${ControllerPath.BYE}") ~> controller.route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[String].contains(route.BYE_MESSAGE) shouldBe true
    }
  }

  "The controller GET /transfer/uuid" should "return json transfer" in {
    Get(s"/${ControllerPath.TRANSFER}/${ControllerPath.BYE}") ~> controller.route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[String].contains(route.CURRENCY) shouldBe true
      responseAs[String].contains(route.SOURCE_ACCOUNT) shouldBe true
      responseAs[String].contains(route.TARGET_ACCOUNT) shouldBe true
    }
  }

  "The controller DELETE /transfer/uuid" should "return json message" in {
    Delete(s"/${ControllerPath.TRANSFER}/${ControllerPath.BYE}") ~> controller.route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[String].contains("dateTime") shouldBe true
      responseAs[String].contains("text") shouldBe true
    }
  }

  "The controller POST /transfer" should "return json message" in {
    Post(s"/${ControllerPath.TRANSFER}", route.buildTransfer()) ~> controller.route ~> check {
      status shouldEqual StatusCodes.Created
    }
  }
}
