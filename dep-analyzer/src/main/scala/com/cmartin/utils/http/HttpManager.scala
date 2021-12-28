package com.cmartin.utils.http

import com.cmartin.utils.Domain.{DomainError, Gav, GavPair}
import zio.{UIO, _}

trait HttpManager {

  def checkDependencies(deps: Iterable[Gav]): UIO[(Iterable[DomainError], Iterable[GavPair])]

  // def checkDependencies(deps: Iterable[Gav]): UIO[Iterable[RepoResult[GavPair]]]

  /* TODO Wait until the integration between Tapir and ZIO
  def checkDependencies(deps: Iterable[Gav]): UIO[Iterable[RepoResult[GavPair]]]

  def shutdown(): UIO[Unit]
   */
}

object HttpManager
    extends Accessible[HttpManager] {

  /** HttpClient infrastructure, connection pool.
    */
  /*
  def managed() =
    ZManaged
      .acquireReleaseWith(ZIO.environment[HttpManager])(_.get.shutdown())
   */

}
