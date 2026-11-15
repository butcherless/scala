package com.cmartin

import java.io.File

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.model.{HttpRequest, HttpResponse}
import org.apache.pekko.stream.scaladsl.{FileIO, Framing}
import org.apache.pekko.util.ByteString

import scala.concurrent.Future
import scala.util.{Failure, Success}

object WebClient extends Greeting {
  def main(args: Array[String]): Unit = {
    implicit val system           = ActorSystem()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(uri = "http://apache.org/"))

    responseFuture
      .onComplete {
        case Success(response) => {
          response.entity.dataBytes
            .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 4096))
            .map(transformEachLine)
            .runWith(FileIO.toPath(new File("/tmp/example.out").toPath))
          println(response.headers.foreach(println(_)))
        }
        case Failure(_)        => sys.error("something wrong")
      }

    def transformEachLine(line: ByteString): ByteString = ByteString(
      line.utf8String.toUpperCase
    )

    Thread.sleep(4000)

    println(s"isCompleted: ${responseFuture.isCompleted}")
    system.terminate()
  }
}

trait Greeting {
  lazy val greeting: String = "pekko-http-client"
}
