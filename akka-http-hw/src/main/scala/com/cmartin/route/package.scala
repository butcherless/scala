package com.cmartin

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

import scala.util.Random


package object route {
  val HOST = "localhost"
  val PORT = 8080
  val CURRENCY = "EUR"
  val HELLO_MESSAGE = "hello from akka http"
  val BYE_MESSAGE = "bye from akka http"

  val logger = LoggerFactory.getLogger("route")

  // domain model
  final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String, id: String)

  // JSON Format, Marshaller & Unmarshaller
  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val transferFormat = jsonFormat5(Transfer)
  }

  // controller

  object ControllerPath {
    val HELLO = "hello"
    val BYE = "bye"
    val TRANSFER = "transfer"
  }

  class ApiController extends Directives with JsonSupport {
    val route =
    /*      path(ControllerPath.HELLO) {
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
             } ~ */
      pathPrefix(ControllerPath.TRANSFER) {
        pathEnd {
          post {
            entity(as[Transfer]) { t =>
              logger.debug(s"post.in: $t")
              complete {
                val entity = t.copy(id = getId())
                buildTextResponse(Created.intValue, s"Entity created ${entity}")
              }
            }
          }
        } ~
          path(IntNumber) { id =>
            get {
              logger.debug(s"get.in: $id")
              val transfer = buildTransfer()
              logger.debug(s"transfer.out: $transfer")

              complete(buildTransfer())
            }
          } ~
          path(IntNumber) { id =>
            put {
              entity(as[Transfer]) { t =>

                complete {
                  logger.debug(s"put.in: $t")
                  val r = new Random().nextDouble()
                  val updated = t.copy(amount = t.amount * r)
                  logger.debug(s"transfer.out: $updated")

                  buildTextResponse(OK.intValue, s"Amount updated to ${updated}")
                }
              }
            }
          } ~
          path( """[a-z0-9-]+""".r) { id =>
            delete {
              complete {
                logger.debug(s"delete.in: $id")
                buildTextResponse(OK.intValue, s"Transfer deleted")
              }
            }
          }
      }
  }

  def buildTextResponse(code: Int, message: String) = {
    HttpResponse(code, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, message))
  }

  def getId() = UUID.randomUUID().toString

  def buildTransfer(): Transfer = Transfer("20950230...1", "01822348...2", BigDecimal.apply(100.0), CURRENCY, getId())


}
