package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.{Done, actor}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object HealthAggregatorWebApp
  extends App
    with ComponentLogging
    with ApiConfiguration {

  implicit val system: ActorSystem[Done] = ActorSystem[Done](Behaviors.setup[Done] { context =>
    implicit lazy val untypedSystem: actor.ActorSystem = context.system.toUntyped
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit lazy val ec: ExecutionContextExecutor = context.system.executionContext

    context.log.info("HealthAggregator ActorSystem started")

    val agent = context.spawn(ServiceActor("service-1"), "service-1")

    lazy val routes: Route = new ApiRoutes(agent).routes

    val serverBinding: Future[Http.ServerBinding] =
      Http()(untypedSystem)
        .bindAndHandle(routes,
          serverConfig.interface,
          serverConfig.port)

    serverBinding.onComplete {
      case Success(bound) =>
        log.info(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
      case Failure(e) =>
        log.error(s"Server could not start!")
        e.printStackTrace()
        context.self ! Done
    }

    Behaviors.receiveMessage {
      case Done =>
        log.error(s"Server could not start!")
        Behaviors.stopped
    }

  }, "typed-actor-system")

}