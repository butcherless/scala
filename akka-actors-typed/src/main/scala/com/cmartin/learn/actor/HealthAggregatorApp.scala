package com.cmartin.learn.actor

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object HealthAggregatorApp {
  def main(args: Array[String]): Unit = {
    ActorSystem[Nothing](
      Behaviors.setup[Nothing] { context =>
        context.log.info("HealthAggregator ActorSystem started")

        context.spawn(HealthAggregator(), "health-aggregator-system")

        Behaviors.empty[Nothing]
      },
      "typed-actor-system"
    )
  }
}
