package com.cmartin.learn.actor

import java.util.UUID

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}

object ServiceActor {
  def apply(serviceId: String): Behavior[ServiceActorCommand] =
    Behaviors.setup(context => new ServiceActor(context, serviceId))

  sealed trait ServiceActorCommand

  case class ServiceCommandTwo(id: UUID) extends ServiceActorCommand

  case class ServiceCommandAsk(number: Long, replayTo: ActorRef[ServiceActorResponse]) extends ServiceActorCommand

  case object Stop extends ServiceActorCommand


  sealed trait ServiceActorResponse

  case class ServiceResponseEven(message: String) extends ServiceActorResponse

  case class ServiceResponseOdd(message: String) extends ServiceActorResponse

}

class ServiceActor(context: ActorContext[ServiceActor.ServiceActorCommand], serviceId: String)
  extends AbstractBehavior[ServiceActor.ServiceActorCommand] {

  import ServiceActor._

  context.log.info("Service Actor {} created", serviceId)

  override def onMessage(message: ServiceActorCommand): Behavior[ServiceActorCommand] = {
    message match {

      case ServiceCommandTwo(id) =>
        context.log.info(s"service command tell received with id: $id")
        Behaviors.same

      case ServiceCommandAsk(number, replayTo) =>
        context.log.info(s"service command ask received with number: $number")
        if (isEven(number))
          replayTo ! ServiceResponseEven(s"even number: $number")
        else
          replayTo ! ServiceResponseOdd(s"oddnumber: $number")
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

