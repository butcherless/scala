package com.cmartin.utils.http

import com.cmartin.utils.config.ConfigHelper.ClientBackend
import com.cmartin.utils.http.HttpManager.{GavResults, retrieveFirstMajor}
import com.cmartin.utils.model.Domain._
import sttp.client3._
import sttp.client3.ziojson._
import zio._

case class ZioHttpManager(client: RIO[Scope, ClientBackend])
    extends HttpManager {

  import ZioHttpManager._

  override def checkDependencies(gavList: Iterable[Gav]): IO[DomainError, GavResults] =
    ZIO.scoped {
      client.flatMap { client =>
        ZIO.partitionPar(gavList)(getDependency(_)(client)).withParallelism(4)
          .map { case (errors, gavList) => GavResults(errors, gavList) }
      }.mapError(e => WebClientError(e.getMessage))
    }

  private def getDependency(gav: Gav)(client: ClientBackend): IO[DomainError, GavPair] = {
    for {
      response   <- makeRequest(gav).send(client)
                      .mapError(e => NetworkError(e.getMessage)) // TODO refactor
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

  def extractDependencies(results: MavenSearchResult): UIO[Seq[Gav]] =
    ZIO.succeed(results.response.docs.map(viewToModel))

}

object ZioHttpManager {

  def viewToModel(a: Artifact): Gav =
    Gav(group = a.g, artifact = a.a, version = a.v)

  val scheme     = "https"
  val searchPath = "search.maven.org/solrsearch/select"
  val resultSize = 10

  val layer =
    ZLayer.fromFunction(client => ZioHttpManager(client))
}
