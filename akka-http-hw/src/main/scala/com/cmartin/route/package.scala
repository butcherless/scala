package com.cmartin

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol


package object route {
  val HOST = "localhost"
  val PORT = 8080
  val CURRENCY = "EUR"
  val HELLO_MESSAGE = "hello from akka http"
  val BYE_MESSAGE = "bye from akka http"

  val logger = LoggerFactory.getLogger("route")

  // domain model
  final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String)

  // JSON Format, Marshaller & Unmarshaller
  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val transferFormat = jsonFormat4(Transfer)
  }

  // controller

  object ControllerPath {
    val HELLO = "hello"
    val BYE = "bye"
    val TRANSFER = "transfer"
  }

  class ApiController extends Directives with JsonSupport {

    val route =
      path(ControllerPath.HELLO) {
        get {
          logger.debug("hello.in")
          // logic goes here
          logger.debug(s"hello.out: $HELLO_MESSAGE")

          complete(buildTextResponse(OK.intValue, HELLO_MESSAGE))
        }
      } ~
        path(ControllerPath.BYE) {
          get {
            complete(buildTextResponse(OK.intValue, BYE_MESSAGE))
          }
        } ~
        path(ControllerPath.TRANSFER / IntNumber) { id =>
          get {
            //complete(buildJsonResponse(200, "good bye from akka http"))
            logger.debug(s"transfer.in: $id")
            val transfer = Transfer("20950230...1", "01822348...2", BigDecimal.apply(100.0), "EUR")
            logger.debug(s"transfer.out: $transfer")

            complete(buildTransfer())
          }
        } ~
        path(ControllerPath.TRANSFER / IntNumber) { id =>
          put {
            entity(as[Transfer]) { t =>
              complete {
                logger.debug(s"transfer.in: $id")
                val amount = t.amount * 0.8
                buildTextResponse(OK.intValue, s"Amount updated to ${amount}")
              }
            }
          }
        }

    /*
       path(ControllerPath.TRANSFER) {
         post {
           entity(as[Transfer]) { t =>
             complete {
               val id = UUID.randomUUID()
               buildTextResponse(Created.intValue, s"Entity with ${id} was created")
             }
           }
         }
       } ~
         path(ControllerPath.TRANSFER / IntNumber) { id =>
           put {
             entity(as[Transfer]) { t =>
               complete {
                 logRequest(t.toString)
                 val amount = t.amount * 0.8
                 buildTextResponse(OK.intValue, s"Amount updated to ${amount}")
               }
             }
           }
         }
           */
  }

  def buildTextResponse(code: Int, message: String) = {
    HttpResponse(code, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, message))
  }


  def buildTransfer(): Transfer = Transfer("20950230...1", "01822348...2", BigDecimal.apply(100.0), CURRENCY)


}
