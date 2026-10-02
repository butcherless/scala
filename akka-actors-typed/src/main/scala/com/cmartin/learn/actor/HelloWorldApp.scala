package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem

object HelloWorldApp {
  def main(args: Array[String]): Unit = {
    val system: ActorSystem[HelloWorldMain.Start] =
      ActorSystem(HelloWorldMain(), "hello")

    system ! HelloWorldMain.Start("World")
    system ! HelloWorldMain.Start("Akka")
  }
}
