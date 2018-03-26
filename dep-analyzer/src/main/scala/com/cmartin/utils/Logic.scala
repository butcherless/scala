package com.cmartin.utils


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
    * Dependency regex
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

  object Dep {
    implicit val ord = new Ordering[Dep] {
      def compare(d1: Dep, d2: Dep): Int = {
        d1.version.compareTo(d2.version)
      }
    }
  }


  /**
    * Parse a string and returns a dependency if it matches a regex
    *
    * @param s a string that can contain a dependency
    * @return a dependency option
    */
  def getDependency(s: String): Option[Dep] = {
    val it = DEP_PATTERN.findAllMatchIn(s)
    if (it.isEmpty) None
    else {
      val gs = it.next()
      Some(Dep(gs.group(GAV_GROUP_POS), gs.group(GAV_ARTIFACT_POS), gs.group(GAV_VERSION_POS)))
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
    s"$RESET$YELLOW$key$RESET ($RESET$MAGENTA${set.size}$RESET) => [$RESET$MAGENTA$s$RESET]"
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
    * An invalid dependency string formatter
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

  def addDependency(dep: Option[Dep]): Boolean = {
    dep match {
      case None => false
      case Some(d) => {
        depList += d
        true
      }
    }
  }

  def getSetByVersionCountGreaterThan(counter: Int) = {
    depList
      .groupBy(_.key)
      .filter(_._2.size > counter)
  }

}

// TODO imprimir el artefacto que usa la dependencia, entrantes