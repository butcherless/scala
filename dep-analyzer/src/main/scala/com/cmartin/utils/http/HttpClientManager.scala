package com.cmartin.utils.http

import com.cmartin.utils.http.HttpManager.{GavResults, retrieveFirstMajor}
import com.cmartin.utils.model.Domain._
import sttp.model.StatusCode
import zio._
import zio.json.DecoderOps

import java.net.URI
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.{HttpClient, HttpRequest}

case class HttpClientManager()
    extends HttpManager {

  import HttpClientManager._

  override def checkDependencies(deps: Iterable[Gav]): IO[DomainError, GavResults] =
    buildManagedClient() { implicit client =>
      ZIO.partitionPar(deps)(getDependency(_)).withParallelism(3)
        .map { case (errors, gavList) => GavResults(errors, gavList) }
    }

  def getDependency(dep: Gav)(implicit client: HttpClient): IO[DomainError, GavPair] = {
    for {
      // TODO refactor line below to a function, see ZioHttpManager.getMavenRequest
      response   <- ZIO.fromCompletableFuture(client.sendAsync(makeRequest(dep), BodyHandlers.ofString()))
                      .orElseFail(NetworkError(s"Connection error while checking dependency: $dep"))
      _          <- ZIO.log(s"http request: ${response.request()}")
      _          <- ZIO.log(s"http status code: ${response.statusCode()}")
      _          <- checkStatusCode(response.statusCode())
      remoteGavs <- extractResults(response.body())
      _          <- ZIO.log(s"remoteGavs(only the first three are shown): ${remoteGavs.take(3)}")
      remoteGav  <- retrieveFirstMajor(remoteGavs, dep)
    } yield GavPair(dep, remoteGav)
  }

  def makeRequest(dep: Gav): HttpRequest =
    HttpRequest.newBuilder().uri(
      URI.create(
        s"$scheme://$path?q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&core=gav&rows=10"
      )
    ).build()

  def acquireClient(): UIO[HttpClient]            = UIO.succeed(HttpClient.newHttpClient())
  lazy val releaseClient: HttpClient => UIO[Unit] = (_: HttpClient) => ZIO.unit

  def buildManagedClient(): ZIO.Release[Any, Nothing, HttpClient] =
    ZIO.acquireReleaseWith(acquireClient())(releaseClient)

  def checkStatusCode(code: Int): IO[DomainError, Option[Nothing]] =
    ZIO.when(!StatusCode(code).isSuccess)(IO.fail(ResponseError(s"status code: $code")))

  def extractResults(body: String): IO[DomainError, Seq[Gav]] = {
    body
      .fromJson[MavenSearchResult] // response body to model
      .fold[IO[DomainError, Seq[Gav]]](
        e => IO.fail(DecodeError(s"Unable to decode response: $e")),
        results =>
          IO.succeed(
            results.response.docs.map { artifact =>
              Gav(group = artifact.g, artifact = artifact.a, version = artifact.v)
            }
          )
      )
  }

  /*
   TODO Wait until the integration between Tapir and ZIO-2-.x
  implicit val backend: SttpBackend[Task, Nothing, NothingT] = ???
   */

  // import HttpManager._
  /* Pattern:
         1. element list to process
         2. processing function
         3. parallelism factor
   */
  /*
  override def checkDependencies(deps: Iterable[Gav]): UIO[Iterable[RepoResult[GavPair]]] = {
    ZIO.foreachPar(deps)(getDependency).withParallelism(2)
  }
   */

  /*
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
   */
  /*
      H E L P E R S
   */

  /*
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
  private def parseResponse(response: Response[Either[String, String]])(dep: Gav): Task[Gav] = {
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
   */

  /* TODO
     a) zio.json decoders

   */

}

object HttpClientManager {

  val scheme = "https"
  val path   = "search.maven.org/solrsearch/select"

  final case class Document(
      id: String,
      g: String,
      a: String,
      latestVersion: String,
      p: String,
      timestamp: Long
  )

  val layer: ULayer[HttpManager] =
    ZLayer.succeed(HttpClientManager())

}
