package com.cmartin.learn.actor

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}
import com.cmartin.learn.actor.HealthAgent.{RequestStatus, RespondStatus}
import com.cmartin.learn.actor.HealthAggregator.{AggregatorMessage, WrappedAgentResponse}

object HealthAggregator {
  /*
    M E S S A G E S
   */
  sealed trait AggregatorMessage

  // Agregator incoming message from external protocol
  case class WrappedAgentResponse(response: HealthAgent.AgentResponse)
      extends AggregatorMessage

  /*
    R E S P O N S E S
   */
  sealed trait AggregatorResponse

  def apply(): Behavior[AggregatorMessage] =
    Behaviors.setup[AggregatorMessage](context => new HealthAggregator(context))
}

class HealthAggregator(context: ActorContext[AggregatorMessage])
    extends AbstractBehavior[AggregatorMessage](context) {
  import DummyInfrastructureManager._

  var agentCount = 3
  context.log.info("HealthAggregator actor created")

  val agentResponseAdapter: ActorRef[HealthAgent.AgentResponse] =
    context.messageAdapter { response =>
      WrappedAgentResponse(response)
    }

  val kafkaAgent: ActorRef[HealthAgent.HealthMessage] =
    context.spawn(HealthAgent(KAFKA_AGENT), "kafka-agent")
  val postgresDbAgent                                 =
    context.spawn(HealthAgent(POSTGRESQL_AGENT), "postgres-agent")
  val systemAgent                                     = context.spawn(HealthAgent(SYSTEM_AGENT), "system-agent")

  kafkaAgent ! RequestStatus(getRequestId(), agentResponseAdapter)
  postgresDbAgent ! RequestStatus(getRequestId(), agentResponseAdapter)
  systemAgent ! RequestStatus(getRequestId(), agentResponseAdapter)

  override def onMessage(
      message: AggregatorMessage
  ): Behavior[AggregatorMessage] = {
    // No need to handle any messages
    message match {
      case WrappedAgentResponse(response) =>
        response match {
          case RespondStatus(requestId, status, _) =>
            context.log.info(
              "Receive response for requesId {} with status {}",
              requestId,
              status
            )
            context.log.debug(s"agent count=$agentCount")
            agentCount = agentCount - 1
            // Behaviors.stopped
            if (agentCount == 0) {
              context.log.debug(s"agent count=$agentCount")
              Behaviors.stopped
            } else {
              Behaviors.same
            }
          case _                                   =>
            Behaviors.same
        }
    }
  }

  override def onSignal: PartialFunction[Signal, Behavior[AggregatorMessage]] = {
    case PostStop =>
      context.log.info("Health aggregator actor stopped")
      this
  }
}
