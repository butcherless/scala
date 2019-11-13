package com.cmartin.utils.environment

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import com.softwaremill.sttp.{Response, Uri, sttp, _}
import io.circe
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.circe.generic.auto._
import io.circe.parser._
import zio.{Task, UIO, ZIO}

trait HttpManagerLive extends HttpManager with HttpClientBackend with ComponentLogging {

  import HttpManager._

  val httpManager: Service[Any] = new HttpManager.Service[Any] {
    /* Pattern:
         1. parallelism factor
         2. element list to process
         3. processing function
    */
    override def checkDependencies(deps: List[Domain.Gav]): UIO[List[RepoResult[Domain.GavPair]]] = {
      ZIO.foreachParN(1)(deps)(getDependency)
    }

    override def shutdown(): UIO[Unit] =
      UIO.effectTotal(backend.close())

    override def getEnvironment(): UIO[Unit] = {
      UIO.unit
    }

    /*
      H E L P E R S
     */

    private def buildUri(dep: Gav): Uri = {
      val filter = s"q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&rows=1&wt=json"
      val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"
      uri"$rawUri"
    }

    private def getDependency(dep: Gav): UIO[RepoResult[GavPair]] = {
      (for {
        response <- sttp.get(buildUri(dep)).send()
        remote <- parseResponse(response)(dep)
      } yield GavPair(dep, remote)).either
    }

    private def parseResponse(response: Response[String])(dep: Gav): Task[Gav] = {
      response.body
        .fold(
          error => Task.fail(new RuntimeException(error)), //TODO domain error
          response => parseResponse(response)
            .fold(
              error => Task.fail(new RuntimeException(s"${error.getMessage} for $dep")), //TODO domain error
              value => Task.succeed(value)
            )
        )
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
            cursor.downField(responseKey).downField(docsKey).downArray.as[Document]
          }
          else
            Left(DecodingFailure("no elements", List(DownField(responseKey), DownField(numFoundKey))))
        }
      } yield Gav(doc.g, doc.a, doc.latestVersion)

      opsResult
    }
  }
}
