package com.cmartin.utils.http

import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import com.softwaremill.sttp.SttpBackend
import zio._

trait HttpManager {
  val httpManager: HttpManager.Service[Any]
}

/**
  * HttpClient infrastructure, connection pool.
  */
trait HttpClientBackend {
  implicit val backend: SttpBackend[Task, Nothing]
}

object HttpManager {

  trait Service[R] {
    def checkDependencies(deps: List[Gav]): ZIO[R, Nothing, List[RepoResult[GavPair]]]

    def shutdown(): ZIO[R, Nothing, Unit]

    def getEnvironment(): ZIO[R, Nothing, Unit]
  }

  final case class Document(
                             id: String,
                             g: String,
                             a: String,
                             latestVersion: String,
                             p: String,
                             timestamp: Long
                           )

}
