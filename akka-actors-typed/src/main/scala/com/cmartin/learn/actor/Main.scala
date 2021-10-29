package com.cmartin.learn.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object Main extends App {
  def delay(delay: Int) = {
    var count = 0
    val timeout = System.currentTimeMillis() + delay

    while (System.currentTimeMillis() < timeout) {
      count = count + 1
    }
    println(count)
  }

  // Create ActorSystem and top level supervisor
  // val system: ActorSystem[Nothing] = ActorSystem[Nothing](IotSupervisor(), "iot-system")

  // system.terminate()

  val greeter: Behavior[String] =
    Behaviors.receiveMessage[String] { message =>
      println(s"Hello $message")
      Behaviors.stopped
    }

  val system: ActorSystem[String] = ActorSystem(greeter, "helloworld")

  system ! "Papuchi"

  // StdIn.readLine()
  delay(1000)
  system.terminate()
}
