package com.cmartin.learn.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

class StreamSpec extends AsyncFlatSpec {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  "Stream spec" should "sum a list of integer via fold sink" in {
    val intSource: Source[Int, NotUsed] = Source[Int](1 to 5)
    val foldSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

    val result = intSource runWith foldSink

    result map {
      sum => assert(sum == 15)
    }
  }

  it should "sum a list of integers via source from future computation" in {
    val ints = List(1, 2, 3, 4, 5)
    val futureSource = Source.fromFuture(Future(ints))
    val foldSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

    val result = futureSource.mapConcat(identity).runWith(foldSink)

    result map { sum => assert(sum == 15) }
  }

}
