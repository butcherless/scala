package com.cmartin.utils

import com.cmartin.utils.Logic._
import org.specs2.mutable.Specification

import scala.collection.SortedSet

class DepAnalyzerSpec extends Specification {

  val dep1 = Dep("dep.group", "dep-artifact", "dep-version-1")
  val dep2 = Dep("dep.group", "dep-artifact", "dep-version-2")

  "get dependency empty string" >> {
    val s = "non-dependency-line"
    val result = getDependency(s)
    result must beNone
  }

  "get dependency string  match" >> {
    val s = "+--- org.springframework.boot:spring-boot-starter-web -> 1.5.10.RELEASE (*)"
    val result = getDependency(s)
    result must beSome
  }

  "dependency key must be group:artifact" >> {
    val group = "org.springframework.boot"
    val artifact = "spring-boot-starter-web"
    val version = "1.5.10.RELEASE"
    val result = Dep(group, artifact, version).key
    result must beEqualTo(s"$group:$artifact")
  }

  "Dependency comparator less than" >> {
    val result = Dep.ord.compare(dep1, dep2)
    result must beLessThan(0)
  }

  "Dependency comparator greater than" >> {
    val result = Dep.ord.compare(dep2, dep1)
    result must beGreaterThan(0)
  }

  "Dependency comparator equals to" >> {
    val result = Dep.ord.compare(dep1, dep1)
    result must beEqualTo(0)
  }

  "mkString formatter string should contain dependency key" >> {
    val key = "dependency-key"
    val set: SortedSet[Dep] = SortedSet[Dep]()
    val result = mkString(key, set)
    result must contain(key)
  }

  "mkErrorString formatter string should contain parameter" >> {
    val message = "error-message"
    val result = mkErrorString(message)
    result must contain(message)
  }

}
