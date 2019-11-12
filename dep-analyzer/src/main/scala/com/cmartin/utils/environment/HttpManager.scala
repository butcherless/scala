package com.cmartin.utils.environment

import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import com.softwaremill.sttp.SttpBackend
import zio._

trait HttpManager {
  val httpManager: HttpManager.Service[Any]
}

trait HttpClientBackend {
  implicit val backend: SttpBackend[Task, Nothing]
}

object HttpManager {

  case class Document(
                       id: String,
                       g: String,
                       a: String,
                       latestVersion: String,
                       p: String,
                       timestamp: Long
                     )

  trait Service[R] {
    def checkDependencies(deps: List[Gav]): ZIO[R, Nothing, List[RepoResult[GavPair]]]

    def shutdown(): ZIO[R, Nothing, Unit]

    def getEnvironment(): ZIO[R, Nothing, Unit]
  }

}
