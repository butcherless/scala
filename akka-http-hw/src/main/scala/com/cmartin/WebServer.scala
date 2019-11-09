package com.cmartin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.cmartin.route.{ApiController, HOST, PORT}
import org.slf4j.LoggerFactory

import scala.io.StdIn

final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String)

object WebServer extends App {
  val logger     = LoggerFactory.getLogger("WebServer")
  val controller = new ApiController()

  implicit val system = ActorSystem("my-system")
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val bindingFuture = Http().bindAndHandle(controller.routes, HOST, PORT)

  println(s"Server online at http://${HOST}:${PORT}/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind())                 // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
