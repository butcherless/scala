package com.cmartin.learn.actor

import akka.Done
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.cmartin.learn.actor.ServiceActor.{ServiceActorResponse, ServiceAskCommand, ServiceTellCommand}

import scala.concurrent.Future
import scala.concurrent.duration._

class ApiRoutes(context: ActorContext[Done]) extends ComponentLogging {
  implicit val timeout: Timeout = 3.seconds
  implicit val scheduler = context.system.scheduler

  val serviceActor = context.spawn(ServiceActor("service-1"), "service-1")

  lazy val routes: Route =
    get {
      concat(
        path("tell" / LongNumber) { number =>
          log.info(s"tell get route with number: $number")
          serviceActor ! ServiceTellCommand(getRequestId())
          if (number == 0) serviceActor ! ServiceActor.Stop
          complete(s"tell-get: $number")
        },
        path("ask" / LongNumber) { number =>
          get {
            log.info(s"ask get route with number: $number")
            val askResponse: Future[ServiceActorResponse] =
              serviceActor.ask(replyTo => ServiceAskCommand(number, replyTo))

            onSuccess(askResponse) { response =>
              complete(response.toString)
            }
          }
        },
        path("shutdown") {
          context.self ! Done
          complete("Shutdown")
        }
      )
    }
}
