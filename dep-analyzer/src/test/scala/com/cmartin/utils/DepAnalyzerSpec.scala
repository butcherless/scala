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

  //TODO repository must be class instead of object

  /*
  "repository should return false when Option is None" >> {
    val previousSize = DependencyRepository.size
    val depOption = Option.empty
    val result = DependencyRepository.addDependency(depOption)
    val currentSize = DependencyRepository.size
    result must beFalse
    currentSize must beEqualTo(previousSize)
  }

  "repository should return true when Option is Some" >> {
    val previousSize = DependencyRepository.size
    val depOption = Some(dep1)
    val result = DependencyRepository.addDependency(depOption)
    val currentSize = DependencyRepository.size
    result must beTrue
    currentSize must beEqualTo(previousSize + 1)
  }

    "filter should return empty map" >> {
      println(s"size: ${DependencyRepository.size}")
      val result1 = DependencyRepository.getSetByVersionCountGreaterThan(1)
      println(result1)
      result1.size must beEqualTo(0)
    }
 */
}
