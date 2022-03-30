package com.cmartin.utils.http

import com.cmartin.utils.http.HttpManager.GavResults
import com.cmartin.utils.model.Domain.{DomainError, Gav, GavPair, ResponseError}
import zio._

import scala.util.matching.Regex

trait HttpManager {

  def checkDependencies(gavList: Iterable[Gav]): IO[DomainError, GavResults]

}

object HttpManager extends Accessible[HttpManager] {

  case class GavResults(errors: Iterable[DomainError], gavList: Iterable[GavPair])

  // extract major version number
  val majorVersionRegex: Regex = raw"(^[0-9]+)..*".r

  def retrieveFirstMajor(gavs: Seq[Gav], gav: Gav): IO[DomainError, Gav] = {
    majorVersionRegex.findFirstMatchIn(gav.version)
      .fold[IO[DomainError, Gav]](
        IO.fail(ResponseError(s"no major version number found for: $gav"))
      )(regexMatch =>
        gavs.find(d => d.version.startsWith(regexMatch.group(1)))
          .fold[IO[DomainError, Gav]](
            IO.fail(ResponseError(s"no remote dependency found for: $gav"))
          )(IO.succeed(_))
      )
  }

}
