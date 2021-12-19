package com.cmartin.utils.http

import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import io.circe
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.circe.generic.auto._
import io.circe.parser.parse
import sttp.client._
import sttp.model.Uri
import zio._

case class HttpManagerLive()
    extends HttpManager {

  import HttpManagerLive.Document

  implicit val backend: SttpBackend[Task, Nothing, NothingT] = ???

  // import HttpManager._
  /* Pattern:
         1. parallelism factor
         2. element list to process
         3. processing function
   */
  override def checkDependencies(deps: List[Domain.Gav]): UIO[List[RepoResult[Domain.GavPair]]] = {
    ZIO.foreachPar(deps)(getDependency)
  }

  override def shutdown(): UIO[Unit] = {
    for {
      _ <- ZIO.logInfo("shutting down http resources")
      _ <- UIO.succeed(
        backend
          .close()
          .catchAll(_ => UIO.unit)
      )
    } yield ()
  }

  /*
      H E L P E R S
   */

  private def buildUri(dep: Gav): Uri = {
    val filter =
      s"q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&rows=1&wt=json"
    val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"
    uri"$rawUri"
  }

  private def getDependency(dep: Gav): UIO[RepoResult[GavPair]] = {
    (for {
      response <- basicRequest.get(buildUri(dep)).send()
      remote <- parseResponse(response)(dep)
    } yield GavPair(dep, remote)).either
  }

  private def parseResponse(
      response: Response[Either[String, String]]
  )(dep: Gav): Task[Gav] = {
    response.body match {
      case Left(error) =>
        Task.fail(new RuntimeException(error)) // TODO domain error
      case Right(response) =>
        parseResponse(response).fold(
          error =>
            Task.fail(
              new RuntimeException(s"${error.getMessage} for $dep")
            ), // TODO domain error
          value => Task.succeed(value)
        )
    }
  }

  private def parseResponse(response: String): Either[circe.Error, Gav] = {
    val numFoundKey = "numFound"
    val responseKey = "response"
    val docsKey = "docs"
    val opsResult: Either[circe.Error, Gav] = for {
      json <- parse(response)
      cursor = json.hcursor
      count <- cursor.downField(responseKey).get[Int](numFoundKey)
      doc <- {
        if (count > 0) {
          cursor
            .downField(responseKey)
            .downField(docsKey)
            .downArray
            .as[Document]
        } else
          Left(
            DecodingFailure(
              "no elements",
              List(DownField(responseKey), DownField(numFoundKey))
            )
          )
      }
    } yield Gav(doc.g, doc.a, doc.latestVersion)

    opsResult
  }
}

object HttpManagerLive {

  final case class Document(
      id: String,
      g: String,
      a: String,
      latestVersion: String,
      p: String,
      timestamp: Long
  )

  val layer: ZLayer[Any, Nothing, HttpManager] =
    ZLayer.fromZIO(UIO.succeed(HttpManagerLive()))

}
