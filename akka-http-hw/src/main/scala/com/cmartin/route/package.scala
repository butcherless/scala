package com.cmartin

import java.time.LocalDateTime
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives
import org.slf4j.LoggerFactory
import spray.json.{DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonFormat}

import scala.util.Random

package object route {
  val HOST           = "localhost"
  val PORT           = 8080
  val HELLO_MESSAGE  = "hello from akka http"
  val BYE_MESSAGE    = "bye from akka http"
  val CURRENCY       = "EUR"
  val SOURCE_ACCOUNT = "source"
  val TARGET_ACCOUNT = "target"
  val ID_NAME        = "id"
  val AMOUNT_NAME    = "amount"
  val DATE_TIME_NAME = "dateTime"
  val TEXT_NAME      = "text"

  val logger = LoggerFactory.getLogger("route")

  // domain model
  final case class Message(text: String, dateTime: LocalDateTime)

  final case class Transfer(
      source: String,
      target: String,
      amount: BigDecimal,
      currency: String,
      id: String
  )

  // JSON Format, Marshaller & Unmarshaller
  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val transferFormat: RootJsonFormat[Transfer] = jsonFormat5(Transfer)

    implicit object MessageJsonFormat extends RootJsonFormat[Message] {
      override def read(json: JsValue): Message = ???

      override def write(message: Message) =
        JsObject(
          "dateTime" -> JsString(message.dateTime.toString),
          "text"     -> JsString(message.text)
        )
    }

  }

  // controller

  object ControllerPath {
    val HELLO    = "hello"
    val BYE      = "bye"
    val TRANSFER = "transfer"
    val ID_REGEX = """[a-z0-9-]+""".r
  }

  class ApiController extends Directives with JsonSupport {
    val routes =
      path(ControllerPath.HELLO) {
        get {
          logger.debug("hello.in")
          // logic goes here
          logger.debug(s"hello.out: $HELLO_MESSAGE")

          complete(OK.intValue, buildMessage(HELLO_MESSAGE))
        }
      } ~
        path(ControllerPath.BYE) {
          get {
            complete(OK.intValue, buildMessage(BYE_MESSAGE))
          }
        } ~
        pathPrefix(ControllerPath.TRANSFER) {
          pathEnd {
            post {
              entity(as[Transfer]) { dto =>
                logger.debug(s"post.in: $dto")
                val entity = dto.copy(id = getId())

                complete(Created.intValue, entity)
              }
            }
          } ~
            path(ControllerPath.ID_REGEX) { id =>
              get {
                logger.debug(s"get.in: $id")
                val transfer = buildTransfer()
                logger.debug(s"transfer.out: $transfer")

                complete(buildTransfer(id))
              }
            } ~
            path(ControllerPath.ID_REGEX) { id =>
              put {
                entity(as[Transfer]) { t =>
                  logger.debug(s"put.in: $t")
                  val r       = new Random().nextDouble()
                  val updated = t.copy(amount = t.amount * r)
                  logger.debug(s"transfer.out: $updated")

                  complete(OK.intValue, updated)
                }
              }
            } ~
            path(ControllerPath.ID_REGEX) { id =>
              delete {
                logger.debug(s"delete.in: $id")
                complete(OK.intValue, buildMessage(id))
              }
            }
        }
  }

  def buildTextResponse(code: Int, message: String) = {
    HttpResponse(
      code,
      entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, message)
    )
  }

  def buildJsonResponse(code: Int, message: String) = {
    HttpResponse(
      code,
      entity = HttpEntity(ContentTypes.`application/json`, message)
    )
  }

  def getId() = UUID.randomUUID().toString

  def buildMessage(text: String) = Message(text, LocalDateTime.now)

  def buildTransfer(): Transfer = buildTransfer(getId())

  def buildTransfer(id: String): Transfer =
    Transfer(
      SOURCE_ACCOUNT,
      TARGET_ACCOUNT,
      BigDecimal.apply(100.0),
      CURRENCY,
      id
    )

}
