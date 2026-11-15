package com.cmartin.learn.actor

import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

object HelloWorld {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted])
  final case class Greeted(whom: String, from: ActorRef[Greet])

  def apply(): Behavior[Greet] =
    Behaviors.receive { (context, message) =>
      context.log.info("Hello {}!", message.whom)
      message.replyTo ! Greeted(message.whom, context.self)
      Behaviors.same
    }
}
