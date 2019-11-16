package com.cmartin.utils

import org.scalatest.{FlatSpec, Matchers}

import scala.util.matching.Regex

/*
  regex for releases and snapshots

  1. local and remote versions should match one of the regex types
  2. local and remote versions should match the same regex type
  3. compare valid local and remote versions
 */
class VersionManagerSpec extends FlatSpec with Matchers {
  import VersionManagerSpec._

  "Regex type release X.Y.Z" should "match the collection of versions" in {
    val pattern: Regex = releaseRegex.r

    val versions = Seq("1.2.3", "10.11.12", "1.2.10", "1.13.1", "10.1.0")

    versions.map(v => info(s"$v: ${pattern.matches(v)}"))

    val result = versions.forall(version => pattern.matches(version))

    result shouldBe true
  }

  "Regex type release X.Y.Z.Final" should "match the collection of versions" in {
    val pattern: Regex = releaseFinalRegex.r

    val versions = Seq("1.2.3.Final", "10.11.12.Final", "1.2.10.Final", "1.13.1.Final", "10.1.0.Final")

    versions.map(v => info(s"$v: ${pattern.matches(v)}"))

    val result = versions.forall(version => pattern.matches(version))

    result shouldBe true
  }

  "Regex type release candidate" should "match the collection of versions" in {
    val pattern: Regex = rcRegex.r

    val versions = Seq("1.2.3-RC1", "10.11.12-RC11", "1.2.10-RC2", "1.13.1-RC5", "10.1.0-RC15")

    versions.map(v => info(s"$v: ${pattern.matches(v)}"))

    val result = versions.forall(version => pattern.matches(version))

    result shouldBe true
  }

  "Release version" should "match one of the regex in the collection" in {
    val version: String       = "1.2.3"
    val result: Option[Regex] = regexSet.find(pattern => pattern.matches(version))

    result.nonEmpty shouldBe true
    result.map { regex =>
      info(regex.toString())
      regex.toString() shouldBe releaseRegex.r.toString()
    }
  }

  "Release Final version" should "match one of the regex in the collection" in {
    val version: String       = "1.2.3.Final"
    val result: Option[Regex] = regexSet.find(pattern => pattern.matches(version))

    result.nonEmpty shouldBe true
    result.map { regex =>
      info(regex.toString())
      regex.toString() shouldBe releaseFinalRegex.r.toString()
    }
  }

  "Release candidate version" should "match one of the regex in the collection" in {
    val version: String       = "1.2.3-RC1"
    val result: Option[Regex] = regexSet.find(pattern => pattern.matches(version))

    result.nonEmpty shouldBe true
    result.map { regex =>
      info(regex.toString())
      regex.toString() shouldBe rcRegex.r.toString()
    }
  }
}

object VersionManagerSpec {
  val releaseBaseRegex  = "[0-9]+.[0-9]+.[0-9]+"
  val releaseRegex      = s"$releaseBaseRegex$$"
  val releaseFinalRegex = s"$releaseBaseRegex.Final$$"
  val rcRegex           = s"$releaseBaseRegex-RC[0-9]+$$"
  val milestoneRegex    = s"$releaseBaseRegex-M[0-9]+$$"

  val regexSet: Set[Regex] = Set(releaseRegex.r, rcRegex.r, releaseFinalRegex.r)
}
