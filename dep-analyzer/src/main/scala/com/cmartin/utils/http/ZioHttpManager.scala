package com.cmartin.utils.http

import com.cmartin.utils.http.HttpManager.{retrieveFirstMajor, GavResults}
import com.cmartin.utils.model.Domain._
import just.semver.SemVer
import just.semver.SemVer.render
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
      response      <- makeRequest(gav).send(client)
                         .mapError(e => NetworkError(e.getMessage)) // TODO refactor
      _             <- ZIO.logDebug(s"status code: ${response.code}")
      remoteGavList <- extractDependencies(response.body)
      _             <- logRemoteGavList(gav, remoteGavList)
      remoteGav     <- retrieveFirstMajor(remoteGavList, gav)
    } yield GavPair(gav, remoteGav)

  private def makeRequest(gav: Gav) =
    basicRequest
      .get(uri"${buildUriFromGav(gav)}")
      .response(asJson[MavenSearchResult].getRight)

  // TODO: manage parse errors, just semver parsing
  // validate artifact properties, fail with domain error
  private def extractDependencies(results: MavenSearchResult): UIO[Seq[Gav]] =
    ZIO.succeed(results.response.docs.map(viewToModel).sorted)

  private def logRemoteGavList(gav: Gav, gavs: Seq[Gav]): UIO[Unit] =
    if (gavs.nonEmpty) {
      ZIO.logDebug(s"remote versions for (g,a)=(${gav.group},${gav.artifact}) -> ${gavs.map(_.version)}")
    } else ZIO.logWarning(s"no remote artifacts for: $gav")

}

object ZioHttpManager {

  def viewToModel(a: Artifact): Gav = {
    val parsedVersion = SemVer.parse(a.v).fold(_ => "", render)

    Gav(group = a.g, artifact = a.a, parsedVersion)
  }
  val scheme                        = "https"
  val searchPath                    = "search.maven.org/solrsearch/select"
  val resultSize                    = 10

  def buildUriFromGav(gav: Gav): String =
    s"$scheme://$searchPath?q=g:${gav.group}+AND+a:${gav.artifact}+AND+p:jar&core=gav&rows=$resultSize"

  val layer =
    ZLayer.fromFunction(client => ZioHttpManager(client))
}
