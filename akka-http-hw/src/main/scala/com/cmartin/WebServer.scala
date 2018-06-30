package com.cmartin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol

import scala.io.StdIn

final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val transferFormat = jsonFormat4(Transfer)
}

object WebServer extends Greeting with JsonSupport {

  val logger = LoggerFactory.getLogger("WebServer")

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val port: Int = 8080

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

            complete(transfer)
          }
        }


    val bindingFuture = Http().bindAndHandle(route, "localhost", port)

    println(s"Server online at http://localhost:${port}/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def buildTextResponse(code: Int, message: String) = {
    HttpResponse(code, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, message))
  }

  def buildJsonResponse(code: Int, message: String) = {
    HttpResponse(code, entity = HttpEntity(ContentTypes.`application/json`, message))
  }
}

trait Greeting {
  lazy val greeting: String = "akka-http-server"
}
