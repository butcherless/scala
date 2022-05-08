package com.cmartin.utils.version

import com.cmartin.utils.model.Domain.ComparatorResult
import com.cmartin.utils.model.Domain.Gav
import zio.{UIO, URIO, ZIO}

trait VersionManager {
  def compare(local: Gav, remote: Gav): UIO[ComparatorResult]
}

object VersionManager {

  def compare(local: Gav, remote: Gav): URIO[VersionManager, ComparatorResult] =
    ZIO.serviceWithZIO[VersionManager](_.compare(local, remote))

}
