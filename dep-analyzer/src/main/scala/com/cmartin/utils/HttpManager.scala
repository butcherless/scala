package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain.Dep
import com.cmartin.utils.HttpManager.Document
import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.zio.AsyncHttpClientZioBackend
import io.circe
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.circe.generic.auto._
import io.circe.parser._
import zio._

final class HttpManager
  extends ComponentLogging {

  implicit val backend = AsyncHttpClientZioBackend()


  def checkDependencies(deps: List[Dep]): UIO[List[Either[Throwable, (Dep,Dep)]]] = {
    ZIO.foreachParN(3)(deps)(getDependency)
  }

  def getDependency(dep: Dep): UIO[Either[Throwable, (Dep,Dep)]] = {
    (for {
      response <- sttp.get(buildUri(dep)).send()
      remote <- parseResponse(response)(dep)
    } yield (dep, remote))
      .either

  }

  def parseResponse(response: Response[String])(dep: Dep): Task[Dep] = {
    response.body match {
      case Left(error) => Task.fail(new RuntimeException(error)) //TODO
      case Right(response) =>
        parseResponse(response) match {
          case Left(value) => Task.fail(new RuntimeException(s"${value.getMessage} for $dep"))
          case Right(value) => Task.succeed(value)
        }
    }
  }

  def parseResponse(response: String): Either[circe.Error, Dep] = {
    val opsResult: Either[circe.Error, Dep] = for {
      json <- parse(response)
      cursor = json.hcursor
      count <- cursor.downField("response").get[Int]("numFound")
      doc <- {
        if (count > 0)
          cursor.downField("response").downField("docs").downArray.as[Document]
        else
          Left(DecodingFailure("no elements", List(DownField("response"), DownField("numFound"))))
      }
    } yield Dep(doc.g, doc.a, doc.latestVersion)

    opsResult
  }

  /*
    def checkDependencies(deps: Seq[Dep]): Unit = {
      val program = ZIO.foreachParN(2)(deps)(getDependency)
      val exec: Seq[Either[Throwable, (Dep, Dep)]] = runtime.unsafeRun(program)

      exec.foreach {
        case Left(value) => log.error(value.getMessage)
        case Right(value) =>
          val (local, remote) = value
          if (local != remote) log.info(value.toString())
      }

      backend.close()
    }


    //TODO translate from UIO[A] => IO[E,A], 1 for comprehension
    def getDependency(dep: Dep): UIO[Either[Throwable, (Dep, Dep)]] = {
      IO.effect {
        val getRequest = sttp
          .get(buildUri(dep))

        log.debug(s"get request: $getRequest")

        val bodyResult: UIO[Either[Throwable, Response[String]]] =
          getRequest
            .send()
            .either
        val exec: Either[Throwable, Response[String]] = runtime.unsafeRun(bodyResult)

        exec match {
          case Right(response) => response.body match {
            case Right(body) =>
              log.debug(body)
              (dep, getDepFromResponse(body))

            case Left(errorMessage) =>
              log.info(s"expected successful result: $errorMessage")
              throw new RuntimeException(errorMessage)
          }
          case Left(exception) =>
            log.info(s"expected successful result: $exception")
            throw exception
        }

      }.either
    }

    def getDepFromResponse(body: String): Dep = {
      val depEither = for {
        json <- parse(body)
        _ <- {
          val cursor = json.hcursor
          cursor.downField("response").get[Int]("numFound").filterOrElse(_ == 1, 0)
        }
        doc <- {
          val cursor = json.hcursor
          cursor.downField("response").downField("docs").downArray.as[Document]
        }
      } yield Dep(doc.g, doc.a, doc.latestVersion)

      depEither.fold(e => throw new RuntimeException(s"unavailable dependency count: $e"), d => d)
    }
  */

  private def buildUri(dep: Dep): Uri = {
    val filter = s"q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&rows=1&wt=json"
    val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"
    uri"$rawUri"
  }

}

object HttpManager {

  case class Document(
                       id: String,
                       g: String,
                       a: String,
                       latestVersion: String,
                       p: String,
                       timestamp: Long
                     )

  case class Response(
                       numFound: Int,
                       start: Int,
                       docs: Seq[Document]
                     )

  def apply(): HttpManager = new HttpManager()

  case class HttpBinResponse(origin: String, headers: Map[String, String])


}