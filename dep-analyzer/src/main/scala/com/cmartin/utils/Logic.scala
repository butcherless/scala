package com.cmartin.utils


import scala.collection.SortedSet
import scala.collection.mutable.SortedMap

object Logic {

  import scala.Console.{MAGENTA, RED, RESET, YELLOW}

  val GAV_ELEMENT_COUNT = 3
  val GAV_GROUP_POS = 1
  val GAV_ARTIFACT_POS = 2
  val GAV_VERSION_POS = 3

  type DepSet = SortedSet[String]
  type DepMap = SortedMap[String, DepSet]

  val DEP_PATTERN = "([0-9a-z.-]+):([0-9a-z.-]+):([0-9A-Za-z.-]+).*".r

  case class Dep(group: String, artifact: String, version: String) {
    def key = s"$group:$artifact"
  }

  def getDependency(s: String): Option[Dep] = {
    val it = DEP_PATTERN.findAllMatchIn(s)
    if (it.isEmpty) None
    else {
      val gs = it.next()
      Some(Dep(gs.group(GAV_GROUP_POS), gs.group(GAV_ARTIFACT_POS), gs.group(GAV_VERSION_POS)))
    }
  }

  def mkString(k: String, l: DepSet) = {
    val s = l.mkString(", ")
    s"$RESET$YELLOW$k$RESET ($RESET$MAGENTA${l.size}$RESET) => [$RESET$MAGENTA$s$RESET]"
  }

  /*
    * Se agrega la version contenida en el value de la entry del map en la key
    * del map correspondiente a la key de la entry
    *
    * @param deps  dependency list
    * @param entry map entry, key is the group and artifact, value is the version
    * @return dependency map updated just in case
    */


  def mkErrorString(s: String): String = {
    s"dependency format error => $RESET$RED$s$RESET"
  }

}

object DependencyRepository {

  import com.cmartin.utils.Logic.{Dep, DepSet}

  var deps = SortedMap[String, DepSet]()

  def addDependency(dep: Option[Dep]): Boolean = {
    dep match {
      case None => false
      case Some(d) => {
        val exists = deps.contains(d.key)
        val entry = deps.getOrElse(d.key, List[String]())
        println(s"$d -> exists($exists), count(${entry.size})")
        deps += ((d.key, deps.getOrElse(d.key, SortedSet[String]()) + d.version))
        true
      }
    }
  }

  def getByVersionCountGreaterThan(c: Int) = {
    deps.filter(_._2.size > c)
  }

  def getAll = deps.toMap

}

// TODO imprimir el artefacto que usa la dependencia, entrantes