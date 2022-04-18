package com.cmartin.utils.version

import com.cmartin.utils.model.Domain.ComparatorResult
import com.cmartin.utils.model.Domain.Gav
import zio.Accessible
import zio.UIO

trait VersionManager {
  def compare(local: Gav, remote: Gav): UIO[ComparatorResult]
}

object VersionManager extends Accessible[VersionManager]
