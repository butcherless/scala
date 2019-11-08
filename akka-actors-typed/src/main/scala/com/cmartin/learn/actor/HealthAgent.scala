package com.cmartin.learn.actor

import java.util.UUID

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}


object HealthAgent {
  def apply(agentId: String): Behavior[HealthMessage] =
    Behaviors.setup(context => new HealthAgent(context, agentId))

  /*
    M E S S A G E S
   */
  sealed trait HealthMessage

  final case class RequestStatus(requestId: UUID, replayTo: ActorRef[RespondStatus])
    extends HealthMessage

  final case object Stop
    extends HealthMessage


  /*
    R E S P O N S E S
   */
  sealed trait AgentResponse

  final case class RespondStatus(requestId: UUID, json: String, replayTo: ActorRef[Stop.type])
    extends AgentResponse


}

class HealthAgent(context: ActorContext[HealthAgent.HealthMessage], agentId: String)
  extends AbstractBehavior[HealthAgent.HealthMessage](context) {

  import DummyInfrastructureManager._
  import HealthAgent._

  var lastStatus: String = ""

  context.log.info("Health agent {} created", agentId)

  override def onMessage(message: HealthMessage): Behavior[HealthMessage] = {
    message match {

      case RequestStatus(requestId, replayTo) =>
        replayTo ! RespondStatus(requestId, getStatus(agentId), context.self)
        context.log.info("Request status with requestId {} and sender {}", requestId, replayTo.path)
        this

      case Stop =>
        Behaviors.stopped
    }
  }

  override def onSignal: PartialFunction[Signal, Behavior[HealthMessage]] = {
    case PostStop =>
      context.log.info("Health agent actor {} stopped", agentId)
      this
  }

  private def getStatus(agentId: String) = agentId match {
    case KAFKA_AGENT => getKafkaStatus()
    case POSTGRESQL_AGENT => getPostgresqlStatus()
    case SYSTEM_AGENT => getSystemStatus()
    case _ => ""
  }
}
