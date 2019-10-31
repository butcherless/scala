package com.cmartin.utils

object Domain {

  type RepoResult[GavPair] = Either[Throwable, GavPair]

  sealed trait DomainError

  case class FileIOError(message: String) extends DomainError

  case class NetworkError(message: String) extends DomainError

  /*
   Version comparator
   */
  sealed trait ComparationResult

  case object Older extends ComparationResult

  case object Same extends ComparationResult

  case object Newer extends ComparationResult

  case class GavPair(local: Gav, remote: Gav) {
    def hasNewVersion(): Boolean =
      local.version != remote.version
  }

  case class Results(pairs: List[RepoResult[GavPair]], validRate: Double)

  /**
    * It represents a maven dependency
    *
    * @param group    dependency group
    * @param artifact dependency artifact
    * @param version  dependency version
    */
  case class Gav(group: String, artifact: String, version: String) {
    def key: String = s"$group:$artifact"

    def formatShort: String = s"$group:$artifact:$version"
  }

  /**
    * Companion Object for Gav case class
    */
  object Gav {
    implicit val ord = new Ordering[Gav] {

      /**
        * Comparator for dependencies classes
        *
        * @param d1 one dependency
        * @param d2 another one dependency
        * @return 0 if equals, -1 if less than, +1 if greater than
        */
      def compare(d1: Gav, d2: Gav): Int = {
        d1.version.compareTo(d2.version)
      }
    }
  }

}
