package com.cmartin.utils.version

import com.cmartin.utils.model.Domain
import zio._

/** Service implementation and collaborators
  */
case class MavenVersionManager()
    extends VersionManager {

  override def compare(local: Domain.Gav, remote: Domain.Gav): UIO[Domain.ComparatorResult] = {
    // TODO naive implementation
    local.version.compareTo(remote.version) match {
      case 0  => ZIO.succeed(Domain.Same)
      case -1 => ZIO.succeed(Domain.Newer)
      case 1  => ZIO.succeed(Domain.Older)
    }
  }

}

object MavenVersionManager {
  val layer: ULayer[VersionManager] =
    ZLayer.succeed(MavenVersionManager())
}
