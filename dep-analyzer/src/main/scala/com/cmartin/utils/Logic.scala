package com.cmartin.utils

import scala.collection.SortedSet
import scala.util.matching.Regex

object Logic {

  import scala.Console.{MAGENTA, RED, RESET, YELLOW}
  import scala.collection.SortedSet

  /**
    * dependency group capture position at the regex
    */
  val GAV_GROUP_POS = 1

  /**
    * dependency artifact capture position at the regex
    */
  val GAV_ARTIFACT_POS = 2

  /**
    * dependency version capture position at the regex
    */
  val GAV_VERSION_POS = 3

  /**
    * Dependency regex root node
    */
  val DEP_ROOT_PATTERN = raw"([0-9a-z.-]+):([0-9a-z.-]+)\s->\s([0-9A-Za-z.-]+).*".r

  /**
    * Dependency regex non root node
    */
  val DEP_PATTERN = "([0-9a-z.-]+):([0-9a-z.-]+):([0-9A-Za-z.-]+).*".r

  /**
    * class that represents a dependency
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

  /**
    * Find string matches against regex list
    *
    * @param s string to match
    * @return match iterator option
    */
  def findMatches(s: String): Option[Iterator[Regex.Match]] = {
    List(DEP_PATTERN, DEP_ROOT_PATTERN).map(_.findAllMatchIn(s)).find(!_.isEmpty)
  }

  /**
    * Parse a string and returns a dependency if it matches a regex
    *
    * @param s a string that could contain a dependency
    * @return a dependency option
    */
  def getDependency(s: String): Option[Dep] = {
    findMatches(s) match {
      case Some(it) => {
        val gs = it.next()
        println(s"gs = $gs")
        Some(Dep(gs.group(GAV_GROUP_POS), gs.group(GAV_ARTIFACT_POS), gs.group(GAV_VERSION_POS)))
      }
      case None => None

    }
  }

  /**
    * A dependency string formatter
    *
    * @param key group and artifact
    * @param set version collection backed by a Set
    * @return the string formatted
    */
  def mkString(key: String, set: SortedSet[Dep]) = {
    val s = set.map(_.version).mkString(", ")
    s"$RESET$YELLOW$key$RESET ($RESET$MAGENTA${set.size}$RESET) => [$RESET$RED$s$RESET]"
  }

  /*
   * Se agrega la version contenida en el value de la entry del map en la key
   * del map correspondiente a la key de la entry
   *
   * @param deps  dependency list
   * @param entry map entry, key is the group and artifact, value is the version
   * @return dependency map updated just in case
   */

  /**
    * A string formatter for n invalid dependencies
    *
    * @param s string to format
    * @return string formatted
    */
  def mkErrorString(s: String): String = {
    s"dependency format error => $RESET$RED$s$RESET"
  }

}

object DependencyRepository {

  import com.cmartin.utils.Logic.Dep

  var depList = scala.collection.mutable.SortedSet[Dep]()

  /**
    * Adds a dependency to the repository
    *
    * @param dep dependency to add
    * @return true if Some(dep)
    */
  def addDependency(dep: Option[Dep]): Boolean = {
    dep match {
      case None => false
      case Some(d) => {
        depList += d
        true
      }
    }
  }

  def getSetByVersionCountGreaterThan(counter: Int): Map[String, SortedSet[Dep]] = {
    depList
      .groupBy(_.key)
      .filter(_._2.size > counter)
  }

  /**
    * Repository number of elements
    *
    * @return dependency count
    */
  def size = {
    depList.size
  }

}

// TODO imprimir el artefacto que usa la dependencia, entrantes
