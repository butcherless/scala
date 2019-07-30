package com.cmartin.learn.actors

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import com.cmartin.learn.actors.Definition.DummyDispatcherActor

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  implicit val system = ActorSystem("IntegerCalc")
  implicit val materializer = ActorMaterializer()

  //implicit val ec = system.dispatcher

  // ask timeout
  implicit val askTimeout = Timeout(5.seconds)

  val numbers: Source[Int, NotUsed] = Source[Int](1 to 20)

  val dummyActor = system.actorOf(DummyDispatcherActor.props())


  //dummyActor ! 7

  numbers
    .ask[Int](dummyActor)
    .runWith(Sink.ignore) // discard response, only for back-pressure

  dummyActor ! "hello"

  //  val processor0 = system.actorOf(NumberProcessor.props(0), "processor0")
  //  val processor5 = system.actorOf(NumberProcessor.props(5), "processor5")
  //  val processors = Seq(processor0, processor5)
  //  val dispatcher = system.actorOf(RequestDispatcher.props(processors), "dispatcher")
  //
  //
  //  val intSource = Source[Int](1 to 10)
  //  val intSink = Sink.foreach(println)

  //intSource.runWith(intSink)


  //  val producer = system.actorOf(Props[Producer], "producer")
  //  val consumer = system.actorOf(Props(classOf[Consumer], producer), "consumer")
  //
  //  producer ! Consumer.Success(3)
  //  producer ! Consumer.Failure("dummy error")
  //
  //  consumer ! Producer.Module(22)

  //

  //dispatcher ! Unknown
  //  dispatcher ! Gromenauer
  //  val result = dispatcher ! Init


  delay(3000)
  Await.ready(system.terminate(), 5 seconds)

}
