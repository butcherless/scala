package com.cmartin.utils.environment

import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.RepoResult
import zio.ZIO

object HttpManagerHelper extends HttpManager.Service[HttpManager] {
  override def checkDependencies(deps: List[Domain.Gav]): ZIO[HttpManager, Nothing, List[RepoResult[Domain.GavPair]]] =
    ZIO.accessM(_.httpManager checkDependencies deps)

  override def shutdown(): ZIO[HttpManager, Nothing, Unit] =
    ZIO.accessM(_.httpManager.shutdown())
}
