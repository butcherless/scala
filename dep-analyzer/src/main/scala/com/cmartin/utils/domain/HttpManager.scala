package com.cmartin.utils.domain

import com.cmartin.utils.domain.HttpManager.GavResults
import com.cmartin.utils.domain.Model.DomainError.ResponseError
import com.cmartin.utils.domain.Model.{DomainError, Gav, GavPair}
import zio._

trait HttpManager {

  def checkDependencies(gavList: Iterable[Gav]): UIO[GavResults]

}

object HttpManager {

  final case class GavResults(errors: Iterable[DomainError], gavList: Iterable[GavPair])

  // extract major version number
  // val majorVersionRegex: Regex = raw"(^[0-9]+)..*".r

  def retrieveFirstMajor(gavs: Seq[Gav], gav: Gav): IO[DomainError, Gav] =
    if (gavs.nonEmpty) ZIO.succeed(gavs.head)
    else ZIO.fail(ResponseError(s"no remote dependency found for: $gav"))

  // ZIO Accessors
  def checkDependencies(gavList: Iterable[Gav]): ZIO[HttpManager, DomainError, GavResults] =
    ZIO.serviceWithZIO[HttpManager](_.checkDependencies(gavList))

}
