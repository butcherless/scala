package com.cmartin.utils

import com.cmartin.utils.Logic._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.SortedSet

class DepAnalyzerSpec extends AnyFlatSpec with Matchers {

  val dep1: Dep = Dep("dep.group", "dep-artifact", "dep-version-1")
  val dep2: Dep = Dep("dep.group", "dep-artifact", "dep-version-2")

  it should "get dependency empty string" in {
    val s      = "non-dependency-line"
    val result = getDependency(s)
    result shouldBe None
  }

  it should "get dependency string  match" in {
    val s      = "+--- org.springframework.boot:spring-boot-starter-web -> 1.5.10.RELEASE (*)"
    val result = getDependency(s)
    result.isDefined shouldBe true
  }

  it should "dependency key must be group:artifact" in {
    val group    = "org.springframework.boot"
    val artifact = "spring-boot-starter-web"
    val version  = "1.5.10.RELEASE"
    val result   = Dep(group, artifact, version).key
    result shouldBe s"$group:$artifact"
  }

  it should "Dependency comparator less than" in {
    val result = Dep.ord.compare(dep1, dep2)
    result shouldBe <(0)
  }

  it should "Dependency comparator greater than" in {
    val result = Dep.ord.compare(dep2, dep1)
    result shouldBe >(0)
  }

  it should "Dependency comparator equals to" in {
    val result = Dep.ord.compare(dep1, dep1)
    result shouldBe 0
  }

  it should "mkString formatter string should contain dependency key" in {
    val key                 = "dependency-key"
    val set: SortedSet[Dep] = SortedSet[Dep]()
    val result              = mkString(key, set)
    result should include(key)
  }

  it should "mkErrorString formatter string should contain parameter" in {
    val message = "error-message"
    val result  = mkErrorString(message)
    result should include(message)
  }

}
