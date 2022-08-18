package com.cmartin.utils.http

import com.cmartin.utils.http.HttpManager.{retrieveFirstMajor, GavResults}
import com.cmartin.utils.model.Domain._
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3._
import sttp.client3.ziojson._
import zio._

case class ZioHttpManager(client: SttpBackend[Task, ZioStreams with WebSockets])
    extends HttpManager {

  import ZioHttpManager._

  override def checkDependencies(gavList: Iterable[Gav]): UIO[GavResults] =
    ZIO.partitionPar(gavList)(getDependency).withParallelism(4)
      .map(GavResults.tupled)

  private def getDependency(gav: Gav): IO[DomainError, GavPair] =
    for {
      response   <- makeRequest(gav).send(client)
                      .mapError(e => NetworkError(e.getMessage)) // TODO refactor
      _          <- ZIO.log(s"status code: ${response.code}")
      remoteGavs <- extractDependencies(response.body)
      _          <- ZIO.logDebug(s"remoteGavs(only the first three are shown): ${remoteGavs.take(3)}")
      remoteGav  <- retrieveFirstMajor(remoteGavs, gav)
    } yield GavPair(gav, remoteGav)

  private def makeRequest(gav: Gav) = {
    basicRequest
      .get(uri"${buildUriFromGav(gav)}")
      .response(asJson[MavenSearchResult].getRight)
  }

  def extractDependencies(results: MavenSearchResult): UIO[Seq[Gav]] =
    ZIO.succeed(results.response.docs.map(viewToModel))

}

object ZioHttpManager {

  def viewToModel(a: Artifact): Gav =
    Gav(group = a.g, artifact = a.a, version = a.v)

  val scheme     = "https"
  val searchPath = "search.maven.org/solrsearch/select"
  val resultSize = 10

  def buildUriFromGav(gav: Gav): String =
    s"$scheme://$searchPath?q=g:${gav.group}+AND+a:${gav.artifact}+AND+p:jar&core=gav&rows=$resultSize"

  val layer =
    ZLayer.fromFunction(client => ZioHttpManager(client))
}
