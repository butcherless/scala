package com.cmartin.utils.environment
import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import com.softwaremill.sttp.{Response, Uri, sttp, _}
import io.circe
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import zio.{Task, UIO, ZIO}

trait HttpManagerLive extends HttpManager with HttpClientBackend with ComponentLogging {
  import HttpManager._
  import io.circe.generic.auto._
  import io.circe.parser._

  val httpManager: Service[Any] = new HttpManager.Service[Any] {
    override def checkDependencies(deps: List[Domain.Gav]): UIO[List[RepoResult[Domain.GavPair]]] = {
      // 1. parallelism factor
      // 2. element list to process
      // 3. processing function
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
        remote   <- parseResponse(response)(dep)
      } yield GavPair(dep, remote)).either
    }

    private def parseResponse(response: Response[String])(dep: Gav): Task[Gav] = {
      response.body match {
        case Left(error) => Task.fail(new RuntimeException(error)) //TODO
        case Right(response) =>
          parseResponse(response) match {
            case Left(value)  => Task.fail(new RuntimeException(s"${value.getMessage} for $dep"))
            case Right(value) => Task.succeed(value)
          }
      }
    }

    private def parseResponse(response: String): Either[circe.Error, Gav] = {
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
  }
}
