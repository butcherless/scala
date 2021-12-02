package com.cmartin.utils.http

import com.cmartin.utils.Domain.Gav
import com.cmartin.utils.Domain.GavPair
import com.cmartin.utils.Domain.RepoResult
import zio._

trait HttpManager {
  def checkDependencies(deps: List[Gav]): UIO[List[RepoResult[GavPair]]]

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
