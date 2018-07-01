package com.cmartin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.cmartin.route.{ApiController, HOST, PORT}
import org.slf4j.LoggerFactory

import scala.io.StdIn

final case class Transfer(source: String, target: String, amount: BigDecimal, currency: String)


object WebServer extends Greeting {

  val logger = LoggerFactory.getLogger("WebServer")
  val controller = new ApiController()

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val bindingFuture = Http().bindAndHandle(controller.route, HOST, PORT)

    println(s"Server online at http://${HOST}:${PORT}/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}

trait Greeting {
  lazy val greeting: String = "akka-http-server"
}
