package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Await

object Main extends App {
  // Create ActorSystem and top level supervisor
  val system: ActorSystem[Nothing] = ActorSystem[Nothing](IotSupervisor(), "iot-system")

  system.terminate()
}
