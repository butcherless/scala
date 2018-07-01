package com.cmartin

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol


package object route {
  val HOST = "localhost"
  val PORT = 8080
  val CURRENCY = "EUR"

  val logger = LoggerFactory.getLogger("route")

  // domain model
  final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String)

  // JSON Format, Marshaller & Unmarshaller
  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val transferFormat = jsonFormat4(Transfer)
  }

  // controller

  class ApiController extends Directives with JsonSupport {

    val route =
      path("hello") {
        get {
          logger.debug("hello.in")
          // logig goes here
          val msg = "hello from akka http"
          logger.debug(s"hello.out: $msg")

          complete(buildTextResponse(200, msg))
        }
      } ~
        path("bye") {
          get {
            complete(buildTextResponse(200, "good bye from akka http"))
          }
        } ~
        path("transfer") {
          get {
            //complete(buildJsonResponse(200, "good bye from akka http"))
            logger.debug("transfer.in")
            val transfer = Transfer("20950230...1", "01822348...2", BigDecimal.apply(100.0), "EUR")
            logger.debug(s"transfer.out: $transfer")

            complete(buildTransfer())
          }
        }
  }

  def buildTextResponse(code: Int, message: String) = {
    HttpResponse(code, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, message))
  }


  def buildTransfer(): Transfer = Transfer("20950230...1", "01822348...2", BigDecimal.apply(100.0), CURRENCY)


}
