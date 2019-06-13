package com.cmartin.learn

import akka.actor.{ActorSystem, PoisonPill}
import com.cmartin.learn.actors.NumberProcessor.{Gromenauer, Init}
import com.cmartin.learn.actors.{NumberProcessor, RequestDispatcher}
import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

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
