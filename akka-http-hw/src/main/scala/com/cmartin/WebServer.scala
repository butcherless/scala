package com.cmartin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import spray.json._

import scala.io.StdIn

final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat4(Transfer)
}

object WebServer extends Greeting with JsonSupport {


  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete(buildTextResponse(200, "hello from akka http"))
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
            complete(Transfer("20950230...", "01822348...", BigDecimal.apply(100.0), "EUR"))
          }
        }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
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
