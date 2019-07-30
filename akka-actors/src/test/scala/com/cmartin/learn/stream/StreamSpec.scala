package com.cmartin.learn.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Merge, Sink, Source}
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

  it should "generate a finite random sequence of integers" in {
    val expectedCount = 10
    val intSource: Source[Int, NotUsed] = Source.repeat(NotUsed)
      .map(_ => randomPositiveInt())
      .take(expectedCount)
    val countFlow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ => 1)

    val result: Future[Int] = intSource.via(countFlow).runFold[Int](0)(_ + _)

    result map { count => assert(count == expectedCount) }
  }

  it should "generate a finite random sequence of integers with extended syntax" in {
    val expectedCount = 10
    val intSource = Source.repeat(NotUsed).map(_ => randomPositiveInt())
      .take(expectedCount)
    val countFlow: Flow[Int, Int, NotUsed] = Flow[Int].map(_ => 1)
    val sumSink = Sink.fold[Int, Int](0)(_ + _)

    val result = intSource
      .via(countFlow)
      .runWith(sumSink)

    result map { count => assert(count == expectedCount) }
  }

  it should "merge several sources into a single sink" in {
    val source: Source[Int, NotUsed] = Source[Int](1 to 10)

    val flow1: Flow[Int, Int, NotUsed] = Flow[Int]
      .filter(_ >= 5)
      .map(n => n + n)

    val flow2: Flow[Int, Int, NotUsed] = Flow[Int]
      .map(n => n * n)

    val flow3: Flow[Int, Int, NotUsed] = Flow[Int]
      .map(n => n * n * n)

    val s1: Source[Int, NotUsed] = source.via(flow1).log("s1-log")
    val s2: Source[Int, NotUsed] = source.via(flow2).log("s2-log")
    val s3: Source[Int, NotUsed] = source.via(flow3).log("s3-log")

    val sumSink = Sink.fold[Int, Int](0)(_ + _)

    val result = Source.combine(s1, s2, s3)(Merge(_)).runWith(sumSink)

    result map { sum =>
      assert(sum == 3500)
    }
  }
}
