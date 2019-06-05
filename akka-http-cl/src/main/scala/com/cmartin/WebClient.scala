package com.cmartin

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ FileIO, Framing }
import akka.util.ByteString

import scala.concurrent.Future
import scala.util.{ Failure, Success }

object WebClient extends Greeting {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://apache.org/"))

    responseFuture
      .onComplete {
        case Success(response) => {
          response.entity.dataBytes
            .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 4096))
            .map(transformEachLine)
            .runWith(FileIO.toPath(new File("/tmp/example.out").toPath))
          println(response.headers.foreach(println(_)))
        }
        case Failure(_) => sys.error("something wrong")
      }

    def transformEachLine(line: ByteString): ByteString = ByteString(line.utf8String.toUpperCase)

    Thread.sleep(4000)

    println(s"isCompleted: ${responseFuture.isCompleted}")
    materializer.shutdown()
    system.terminate()
  }
}

trait Greeting {
  lazy val greeting: String = "akka-http-client"
}