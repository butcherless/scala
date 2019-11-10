package com.cmartin.utils.environment

import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import zio._

trait HttpManager {
  val httpManager: HttpManager.Service[Any]
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
  }
}
