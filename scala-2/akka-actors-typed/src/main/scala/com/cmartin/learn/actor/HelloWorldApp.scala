package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem

object HelloWorldApp extends App {
  val system: ActorSystem[HelloWorldMain.Start] =
    ActorSystem(HelloWorldMain(), "hello")

  system ! HelloWorldMain.Start("World")
  system ! HelloWorldMain.Start("Akka")
}
