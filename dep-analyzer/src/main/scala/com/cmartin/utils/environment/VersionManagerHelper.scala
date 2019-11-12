package com.cmartin.utils.environment

import com.cmartin.utils.Domain
import zio.ZIO

object VersionManagerHelper extends VersionManager.Service[VersionManager] {

  override def compare(local: Domain.Gav, remote: Domain.Gav): ZIO[VersionManager, Nothing, Domain.ComparationResult] = {
    ZIO.accessM(_.versionManager compare(local, remote))
  }

}
