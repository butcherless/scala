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

}
