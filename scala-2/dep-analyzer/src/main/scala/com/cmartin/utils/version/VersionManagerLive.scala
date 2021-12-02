package com.cmartin.utils.version

import com.cmartin.utils.Domain
import zio._

/** Service implementation and collaborators
  */
case class VersionManagerLive(versionManager: VersionManager)
    extends VersionManager {

  override def compare(local: Domain.Gav, remote: Domain.Gav): UIO[Domain.ComparationResult] = {
    // TODO naive implementation
    local.version.compareTo(remote.version) match {
      case 0  => UIO.succeed(Domain.Same)
      case -1 => UIO.succeed(Domain.Newer)
      case 1  => UIO.succeed(Domain.Older)
    }
  }

}

object VersionManagerLive {
  val layer =
    (VersionManagerLive(_)).toLayer
}
