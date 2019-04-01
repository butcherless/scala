package com.cmartin.learn

import akka.actor.{ActorSystem, Props}
import com.cmartin.learn.actors.{Consumer, Producer}

import scala.concurrent.duration._

object Main extends App {
  val system = ActorSystem("IntegerCalc")

  val producer = system.actorOf(Props[Producer], "producer")
  val consumer = system.actorOf(Props(classOf[Consumer],producer), "consumer")

  producer ! Consumer.Success(3)
  producer ! Consumer.Failure("dummy error")

  consumer ! Producer.Module(22)

}
