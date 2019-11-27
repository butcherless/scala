package com.cmartin.learn.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object HelloWorldMain {
  final case class Start(name: String)

  def apply(): Behavior[Start] =
    Behaviors.setup { context =>
      val greeter = context.spawn(HelloWorld(), "greeter")

      Behaviors.receiveMessage { message =>
        val replyTo = context.spawn(HelloWorldBot(max = 3), message.name)
        greeter ! HelloWorld.Greet(message.name, replyTo)
        Behaviors.same
      }
    }
}
