package com.cmartin.utils

import zio.json.{DeriveJsonDecoder, JsonDecoder}

import scala.util.matching.Regex

object Domain {
  /*
    CONSTANT MESSAGES
   */

  val OPEN_FILE_ERROR = "Error while opening the file"
  val FILE_BUFFER_ERROR = "Error while creating the file buffer"

  sealed trait DomainError

  case class FileIOError(message: String) extends DomainError

  case class NetworkError(message: String) extends DomainError
  case class ResponseError(message: String) extends DomainError
  case class DecodeError(message: String) extends DomainError

  case class UnknownError(m: String) extends DomainError

  /*
   Version comparator
   */
  sealed trait ComparationResult

  case object Older extends ComparationResult

  case object Same extends ComparationResult

  case object Newer extends ComparationResult

  case class GavPair(local: Gav, remote: Gav) {
    def hasNewVersion: Boolean =
      local.version != remote.version
  }

  /** It represents a maven dependency
    *
    * @param group
    *   dependency group
    * @param artifact
    *   dependency artifact
    * @param version
    *   dependency version
    */
  case class Gav(group: String, artifact: String, version: String) {
    def key: String = s"$group:$artifact"

    def formatShort: String = s"$group:$artifact:$version"
  }

  /** Companion Object for Gav case class
    */
  object Gav {
    implicit val decoder: JsonDecoder[Gav] = DeriveJsonDecoder.gen[Gav]

    implicit val ord: Ordering[Gav] = new Ordering[Gav] {

      /** Comparator for dependencies classes
        *
        * @param d1
        *   one dependency
        * @param d2
        *   another one dependency
        * @return
        *   0 if equals, -1 if less than, +1 if greater than
        */
      def compare(d1: Gav, d2: Gav): Int = {
        d1.version.compareTo(d2.version)
      }
    }

    def fromRegexMatch(regexMatch: Regex.Match): Gav = {
      Gav(
        regexMatch.group(1), // group
        regexMatch.group(2), // artifact
        regexMatch.group(3) // version
      )
    }
  }

  case class MavenSearchResult(
      responseHeader: ResponseHeader,
      response: Response
  )

  object MavenSearchResult {
    implicit val decoder: JsonDecoder[MavenSearchResult] = DeriveJsonDecoder.gen[MavenSearchResult]

  }

  case class ResponseHeader(
      status: Int,
      params: Params
  )

  object ResponseHeader {
    implicit val decoder: JsonDecoder[ResponseHeader] = DeriveJsonDecoder.gen[ResponseHeader]

  }

  case class Params(
      q: String,
      core: String,
      fl: String,
      sort: String,
      rows: Int,
      wt: String,
      version: String
  )

  object Params {
    implicit val decoder: JsonDecoder[Params] = DeriveJsonDecoder.gen[Params]

  }

  case class Response(
      numFound: Int,
      start: Int,
      docs: Seq[Artifact]
  )

  object Response {
    implicit val decoder: JsonDecoder[Response] = DeriveJsonDecoder.gen[Response]

  }

  case class Artifact(
      id: String,
      g: String,
      a: String,
      v: String,
      p: String,
      timestamp: Long,
      ec: Seq[String]
  )

  object Artifact {
    implicit val decoder: JsonDecoder[Artifact] = DeriveJsonDecoder.gen[Artifact]
  }

}
