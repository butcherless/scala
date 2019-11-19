package com.cmartin.utils.version

import com.cmartin.utils.Domain
import com.cmartin.utils.Domain.{ComparationResult, Gav}
import zio.ZIO

/* ZIO Module Steps: [https://zio.dev/docs/howto/howto_use_module_pattern]
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


  object Helper extends VersionManager.Service[VersionManager] {

    override def compare(local: Domain.Gav, remote: Domain.Gav): ZIO[VersionManager, Nothing, Domain.ComparationResult] = {
      ZIO.accessM(_.versionManager compare(local, remote))
    }

  }

}
