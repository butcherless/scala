package com.cmartin.learn.actor

import java.util.UUID

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}

object ServiceActor {
  def apply(serviceId: String): Behavior[ServiceActorCommand] =
    Behaviors.setup(context => new ServiceActor(context, serviceId))

  sealed trait ServiceActorCommand

  case class ServiceTellCommand(id: UUID) extends ServiceActorCommand

  case class ServiceAskCommand(number: Long, replayTo: ActorRef[ServiceActorResponse]) extends ServiceActorCommand

  case object Stop extends ServiceActorCommand

  sealed trait ServiceActorResponse

  case class ServiceEvenResponse(message: String) extends ServiceActorResponse

  case class ServiceOddResponse(message: String) extends ServiceActorResponse
}

class ServiceActor(context: ActorContext[ServiceActor.ServiceActorCommand], serviceId: String)
    extends AbstractBehavior[ServiceActor.ServiceActorCommand](context) {
  import ServiceActor._

  context.log.info("Service Actor {} created", serviceId)

  override def onMessage(message: ServiceActorCommand): Behavior[ServiceActorCommand] = {
    message match {
      case ServiceTellCommand(id) =>
        context.log.info(s"service command tell received with id: $id")
        Behaviors.same

      case ServiceAskCommand(number, replayTo) =>
        context.log.info(s"service command ask received with number: $number")
        if (isEven(number))
          replayTo ! ServiceEvenResponse(s"even number: $number")
        else
          replayTo ! ServiceOddResponse(s"odd number: $number")
        Behaviors.same

      case Stop =>
        Behaviors.stopped
    }
  }

  override def onSignal: PartialFunction[Signal, Behavior[ServiceActorCommand]] = {
    case PostStop =>
      context.log.info("Service Actor {} stopped", serviceId)
      this
  }
}
