package com.cmartin.learn.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.collection.immutable
import scala.concurrent.duration._

object Main extends App {
  println("Main streams")

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val sources = immutable.Seq(Source(List(1, 2, 3)), Source(List(10, 20, 30)))

  //  val done = Source
  //    .combine(sources(0), sources(1))(Concat(_))
  //    .runWith(Sink.foreach(println))
  //val done: Future[Done] = Source(1 to 10).throttle(1, 1 second).runWith(Sink.foreach(println))

  //val r = buildIntSource(1).runWith(printSink)

  val s1 = Source[Int](1 to 10)
  val s2 = Source[Int](11 to 20)
  val onePerSecSource: Source[Int, NotUsed] = s1.throttle(1, 1 second)
  val halfPerSecSource: Source[Int, NotUsed] = s2.throttle(1, 500 milliseconds)

  val printSink = Sink.foreach(println)

  val sHalfFlow = Flow[Int].map(x => 1 / (x - 13)).recover {
    case _ => 0
  }

  //  val fut1 = sOnePerSec.runWith(Sink.foreach(println))
  //  val fut2 = sHalfPerSec.runWith(Sink.foreach(println))

  //  val combineFut = Source
  //    .combine(onePerSecSource, halfPerSecSource)(Merge(_))
  //    .runWith(printSink)

  val zipFut = Source
    //        .zipN(Seq(onePerSecSource, halfPerSecSource.via(sHalfFlow)))
    .zipN(Seq(onePerSecSource, halfPerSecSource))
    .runWith(printSink)


  zipFut.onComplete(_ => system.terminate())
}

