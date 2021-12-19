package com.cmartin

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.cmartin.route.{ApiController, ControllerPath, JsonSupport}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WebServerSpec
    extends AnyFlatSpec
    with Matchers
    with ScalatestRouteTest
    with JsonSupport {
  val JSON_CONTENT_TYPE = ContentTypes.`application/json`
  val ID = "4e4387c4-38e0-4fd8-80cd-2ca7a6395d8e"

  val controller = new ApiController()

  "The controller method GET /hello" should "return hello message" in {
    Get(s"/${ControllerPath.HELLO}") ~> controller.routes ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual JSON_CONTENT_TYPE
      checkMessageResponse(responseAs[String])
    }
  }

  "The controller method GET /bye" should "return bye message" in {
    Get(s"/${ControllerPath.BYE}") ~> controller.routes ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual JSON_CONTENT_TYPE
      checkMessageResponse(responseAs[String])
    }
  }

  "The controller method GET /transfer/uuid" should "return json transfer" in {
    Get(s"/${ControllerPath.TRANSFER}/${ID}") ~> controller.routes ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual JSON_CONTENT_TYPE
      checkTransferResponse(responseAs[String])
    }
  }

  "The controller method DELETE /transfer/uuid" should "return json message" in {
    Delete(s"/${ControllerPath.TRANSFER}/${ID}") ~> controller.routes ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual JSON_CONTENT_TYPE
      checkMessageResponse(responseAs[String])
    }
  }

  "The controller method POST /transfer" should "return json message" in {
    Post(
      s"/${ControllerPath.TRANSFER}",
      route.buildTransfer()
    ) ~> controller.routes ~> check {
      status shouldEqual StatusCodes.Created
      contentType shouldEqual JSON_CONTENT_TYPE
      checkTransferResponse(responseAs[String])
    }
  }

  "The controller method PUT /transfer" should "return json message" in {
    Put(
      s"/${ControllerPath.TRANSFER}/${ID}",
      route.buildTransfer()
    ) ~> controller.routes ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual JSON_CONTENT_TYPE
      checkTransferResponse(responseAs[String])
    }
  }

  def checkTransferResponse(json: String) = {
    json.contains(route.CURRENCY) shouldBe true
    json.contains(route.SOURCE_ACCOUNT) shouldBe true
    json.contains(route.TARGET_ACCOUNT) shouldBe true
    json.contains(route.ID_NAME) shouldBe true
    json.contains(route.AMOUNT_NAME) shouldBe true
  }

  def checkMessageResponse(json: String) = {
    json.contains(route.TEXT_NAME) shouldBe true
    json.contains(route.DATE_TIME_NAME) shouldBe true
  }
}
