package com.cmartin.utils.http

import com.cmartin.utils.Domain.{Gav, GavPair, RepoResult}
import zio._

trait HttpManager {
  def checkDependencies(deps: Iterable[Gav]): UIO[Iterable[RepoResult[GavPair]]]

  def shutdown(): UIO[Unit]
}

object HttpManager
    extends Accessible[HttpManager] {

  /** HttpClient infrastructure, connection pool.
    */

  def managed() =
    ZManaged
      .acquireReleaseWith(ZIO.environment[HttpManager])(_.get.shutdown())

}
