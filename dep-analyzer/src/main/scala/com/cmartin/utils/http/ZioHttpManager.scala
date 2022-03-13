package com.cmartin.utils.http

import com.cmartin.utils.http.HttpManager.retrieveFirstMajor
import com.cmartin.utils.model.Domain._
import sttp.client3._
import sttp.client3.asynchttpclient.zio._
import sttp.client3.ziojson._
import zio._

final case class ZioHttpManager()
    extends HttpManager {

  private val managedClient = AsyncHttpClientZioBackend().toManaged

  import ZioHttpManager._

  override def checkDependencies(gavs: Iterable[Gav]): UIO[(Iterable[DomainError], Iterable[GavPair])] =
    ZIO.partitionPar(gavs)(getDependency).withParallelism(3)

  def getDependency(gav: Gav): IO[DomainError, GavPair] = {
    for {
      response   <- getMavenRequest(gav)
      _          <- ZIO.log(s"status code: ${response.code}")
      remoteGavs <- extractDependencies(response.body)
      _          <- ZIO.log(s"remoteGavs(only the first three are shown): ${remoteGavs.take(3)}")
      remoteGav  <- retrieveFirstMajor(remoteGavs, gav)
    } yield GavPair(gav, remoteGav)

  }

  private def makeRequest(dep: Gav) = {
    val uri = s"$scheme://$searchPath?q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&core=gav&rows=$resultSize"
    basicRequest
      .get(uri"$uri")
      .response(asJson[MavenSearchResult].getRight)
  }

  private def getMavenRequest(gav: Gav): IO[NetworkError, Response[MavenSearchResult]] =
    managedClient.use { b =>
      makeRequest(gav).send(b)
    }.mapError(e => NetworkError(e.getMessage))

  def extractDependencies(results: MavenSearchResult): UIO[Seq[Gav]] =
    UIO.succeed(results.response.docs.map(viewToModel))

}

object ZioHttpManager extends (() => HttpManager) {

  def viewToModel(a: Artifact): Gav =
    Gav(group = a.g, artifact = a.a, version = a.v)

  val scheme     = "https"
  val searchPath = "search.maven.org/solrsearch/select"
  val resultSize = 10

  val layer: ULayer[HttpManager] =
    ZioHttpManager.toLayer
}
