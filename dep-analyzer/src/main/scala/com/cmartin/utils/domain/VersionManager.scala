package com.cmartin.utils.domain

import com.cmartin.utils.domain.Model.{ComparatorResult, Gav}
import zio.{UIO, URIO, ZIO}

trait VersionManager {
  def compare(local: Gav, remote: Gav): UIO[ComparatorResult]
}

object VersionManager {

  def compare(local: Gav, remote: Gav): URIO[VersionManager, ComparatorResult] =
    ZIO.serviceWithZIO[VersionManager](_.compare(local, remote))

}
