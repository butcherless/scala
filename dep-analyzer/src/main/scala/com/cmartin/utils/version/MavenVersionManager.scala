package com.cmartin.utils.version

import com.cmartin.utils.domain.Model.ComparatorResult
import com.cmartin.utils.domain.{Model, VersionManager}
import zio._

/** Service implementation and collaborators
  */
final case class MavenVersionManager()
    extends VersionManager {

  override def compare(local: Model.Gav, remote: Model.Gav): UIO[ComparatorResult] = {
    // TODO naive implementation
    local.version.compareTo(remote.version) match {
      case 0  => ZIO.succeed(ComparatorResult.Same)
      case -1 => ZIO.succeed(ComparatorResult.Newer)
      case 1  => ZIO.succeed(ComparatorResult.Older)
    }
  }

}

object MavenVersionManager {
  val layer: ULayer[VersionManager] =
    ZLayer.succeed(MavenVersionManager())
}
