package com.cmartin.learn.actors

import akka.actor.ActorSystem
import com.cmartin.learn.actors.NumberProcessor.{Gromenauer, Init}

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  val system = ActorSystem("IntegerCalc")

  //  val producer = system.actorOf(Props[Producer], "producer")
  //  val consumer = system.actorOf(Props(classOf[Consumer], producer), "consumer")
  //
  //  producer ! Consumer.Success(3)
  //  producer ! Consumer.Failure("dummy error")
  //
  //  consumer ! Producer.Module(22)

  val processor = system.actorOf(NumberProcessor.props(5), "processor")
  val dispatcher = system.actorOf(RequestDispatcher.props(processor), "dispatcher")

  dispatcher ! Gromenauer
  val result = dispatcher ! Init


  Await.ready(system.terminate(), 5 seconds)

}
