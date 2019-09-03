package com.cmartin.learn.actor

import akka.actor.ActorSystem
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.cmartin.learn.actor.ServiceActor.{ServiceActorResponse, ServiceCommandAsk, ServiceCommandTwo}

import scala.concurrent.Future
import scala.concurrent.duration._


class ApiRoutes(serviceActor: ActorRef[ServiceActor.ServiceActorCommand])(implicit system: ActorSystem)
  extends ComponentLogging {

  implicit val timeout: Timeout = 3.seconds
  implicit val scheduler = system.scheduler

  lazy val routes: Route =
    get {
      concat(
        path("tell" / LongNumber) { number =>
          log.info(s"tell get route with number: $number")
          serviceActor ! ServiceCommandTwo(getRequestId())
          if (number == 0) serviceActor ! ServiceActor.Stop
          complete(s"tell-get: $number")
        },
        path("ask" / LongNumber) { number =>
          get {
            log.info(s"ask get route with number: $number")
            val askResponse: Future[ServiceActorResponse] =
              serviceActor.ask(replyTo => ServiceCommandAsk(number, replyTo))

            onSuccess(askResponse) { response =>
              complete(response.toString)
            }
          }
        }
      )
    }
}