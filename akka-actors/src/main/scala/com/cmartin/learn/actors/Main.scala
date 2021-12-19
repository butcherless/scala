package com.cmartin.learn.actors

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import akka.{Done, NotUsed}
import com.cmartin.learn.actors.Definition.DispatcherActor.Stats
import com.cmartin.learn.actors.Definition._
import com.cmartin.learn.stream.buildRandomPositiveIntSource

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends App {
  implicit val system = ActorSystem("IntegerCalc")

  // ask timeout
  implicit val askTimeout = Timeout(2.seconds)

  val numbers: Source[Int, NotUsed] = Source[Int](1 to 20)

  // create the workers
  val workers =
    for (i <- 1 to 10)
      yield system.actorOf(IntegerProcessor.props(i - 1), s"worker-${i - 1}")

  val dispatcherActor =
    system.actorOf(DispatcherActor.props(workers), "dispatcher")

  val intSourceResult: Future[Done] = buildRandomPositiveIntSource()
    .take(100)
    .ask[Int](5)(dispatcherActor)
    .runWith(Sink.ignore) // discard response, only for back-pressure

  import akka.pattern.ask

  Await.ready(intSourceResult, Duration.Inf)
  val stats = Await.result(dispatcherActor ? Stats, 1 second)
  Thread.sleep(1000)
  Await.ready(system.terminate(), 1 second)

}
