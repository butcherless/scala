package com.cmartin.utils

object Domain {

  sealed trait DomainError

  case class FileIOError(message: String) extends DomainError

  case class NetworkError(message: String) extends DomainError

  /**
    * It represents a maven dependency
    *
    * @param group    dependency group
    * @param artifact dependency artifact
    * @param version  dependency version
    */
  case class Dep(group: String, artifact: String, version: String) {
    def key = s"$group:$artifact"
  }

  /**
    * Companion Object for Dep case class
    */
  object Dep {
    implicit val ord = new Ordering[Dep] {

      /**
        * Comparator for dependencies classes
        *
        * @param d1 one dependency
        * @param d2 another one dependency
        * @return 0 if equals, -1 if less than, +1 if greater than
        */
      def compare(d1: Dep, d2: Dep): Int = {
        d1.version.compareTo(d2.version)
      }
    }
  }

}
