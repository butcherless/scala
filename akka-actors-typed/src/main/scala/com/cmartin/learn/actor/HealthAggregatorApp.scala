package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object HealthAggregatorApp
  extends App {

  ActorSystem[Nothing](Behaviors.setup[Nothing] { context =>
    context.log.info("HealthAggregator ActorSystem started")

    context.spawn(HealthAggregator(), "health-aggregator-system")

    Behaviors.empty[Nothing]
  }, "typed-actor-system")
}
