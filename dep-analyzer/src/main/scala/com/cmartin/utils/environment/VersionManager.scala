package com.cmartin.utils.environment

import com.cmartin.utils.Domain.{ComparationResult, Gav}
import zio.ZIO

/* Steps:
  - create VersionManager object.
  - create VersionManager.Service[R] trait.
  - create VersionManager trait.
  - create VersionManagerLive trait which extends VersionManager. Add collaborators if needed.
  - create VersionManagerLive object which extends VersionManagerLive trait
  - create an instance of VersionManager.Service[Any] trait and implement its definition.
  - create VersionManagerHelper object which extends VersionManager.Service[VersionManager].
 */

trait VersionManager {
  val versionManager: VersionManager.Service[Any]
}

object VersionManager {

  /**
    * Service operations
    *
    * @tparam R the environment
    */
  trait Service[R] {
    def compare(local: Gav, remote: Gav): ZIO[R, Nothing, ComparationResult]
  }

}
