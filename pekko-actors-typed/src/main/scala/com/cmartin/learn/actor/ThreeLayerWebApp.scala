package com.cmartin.learn.actor

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.scaladsl.adapter.*
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.{actor, Done}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object ThreeLayerWebApp
    extends ComponentLogging
    with ApiConfiguration {
  def main(args: Array[String]): Unit = {
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
          Http()(using untypedSystem)
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
}
