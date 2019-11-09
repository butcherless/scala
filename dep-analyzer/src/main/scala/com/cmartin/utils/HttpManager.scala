package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.zio.AsyncHttpClientZioBackend
import io.circe
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.circe.generic.auto._
import io.circe.parser._
import zio._

final class HttpManager extends ComponentLogging {

  import HttpManager._

  implicit val backend: SttpBackend[Task, Nothing] = AsyncHttpClientZioBackend()

  def checkDependencies(deps: List[Gav]): UIO[List[RepoResult[GavPair]]] = {
    // factor paralelización
    // lista de elementos a procesar
    // funcion de procesamiento
    ZIO.foreachParN(2)(deps)(getDependency)
  }

  def getDependency(dep: Gav): UIO[RepoResult[GavPair]] = {
    (for {
      response <- sttp.get(buildUri(dep)).send()
      remote <- parseResponse(response)(dep)
    } yield GavPair(dep, remote)).either
  }

  def parseResponse(response: Response[String])(dep: Gav): Task[Gav] = {
    response.body match {
      case Left(error) => Task.fail(new RuntimeException(error)) //TODO
      case Right(response) =>
        parseResponse(response) match {
          case Left(value)  => Task.fail(new RuntimeException(s"${value.getMessage} for $dep"))
          case Right(value) => Task.succeed(value)
        }
    }
  }

  def parseResponse(response: String): Either[circe.Error, Gav] = {
    val opsResult: Either[circe.Error, Gav] = for {
      json <- parse(response)
      cursor = json.hcursor
      count <- cursor.downField("response").get[Int]("numFound")
      doc <- {
        if (count > 0)
          cursor.downField("response").downField("docs").downArray.as[Document]
        else
          Left(DecodingFailure("no elements", List(DownField("response"), DownField("numFound"))))
      }
    } yield Gav(doc.g, doc.a, doc.latestVersion)

    opsResult

  }

  def shutdown(): UIO[Unit] =
    UIO.effectTotal(backend.close())

  /*
      H E L P E R S
   */

  private def buildUri(dep: Gav): Uri = {
    val filter = s"q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&rows=1&wt=json"
    val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"
    uri"$rawUri"
  }

}

object HttpManager {

  def apply(): HttpManager = new HttpManager()

  case class Document(
      id: String,
      g: String,
      a: String,
      latestVersion: String,
      p: String,
      timestamp: Long
  )

  case class MavenResponse(
      numFound: Int,
      start: Int,
      docs: Seq[Document]
  )

  case class HttpBinResponse(origin: String, headers: Map[String, String])

}
