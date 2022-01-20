package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.{Done, actor}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object ThreeLayerWebApp
    extends App
    with ComponentLogging
    with ApiConfiguration {
  //  implicit val system: ActorSystem[Done] = ActorSystem[Done](Behaviors.setup[Done] { context =>
  ActorSystem[Done](
    Behaviors.setup[Done] { context =>
      implicit lazy val untypedSystem: actor.ActorSystem =
        context.system.toClassic
      implicit lazy val ec: ExecutionContextExecutor     =
        context.system.executionContext

      context.log.info("ThreeLayerWebApp ActorSystem started")

      lazy val routes: Route = new ApiRoutes(context).routes

      val bindingFuture: Future[Http.ServerBinding] =
        Http()(untypedSystem)
          .newServerAt(serverConfig.interface, serverConfig.port)
          .bind(routes)

      bindingFuture.onComplete {
        case Success(bound) =>
          log.info(
            s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/"
          )
        case Failure(e)     =>
          log.error(s"Server could not start!")
          e.printStackTrace()
          context.self ! Done
      }

      Behaviors.receiveMessage { case Done =>
        Thread.sleep(1000)
        Behaviors.stopped
      }
    },
    "typed-actor-system"
  )
}
