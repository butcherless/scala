package com.cmartin.utils

object DependencyAnalyzer extends App {

  import com.cmartin.utils.Logic._

  import scala.collection.mutable.SortedMap
  import scala.io.Source

  val deps = SortedMap[String, List[String]]()

  if (args.isEmpty) System.exit(1)

  val bufferedSource = Source.fromFile(args.toList.head)
  for (line <- bufferedSource.getLines) {
    val gav = getGAV(line)
    if (gav.isDefined) {
      val ga = getGroupAndArtifact(gav.get) // group & artifact
      val ver = getVersion(gav.get) //  version
      val currentVersions = deps.getOrElse(ga, List[String]())
      val newVersions = currentVersions :+ ver
      deps += ((ga, newVersions))
    } else {
      println(mkErrorString(line))
    }
  }
  bufferedSource.close

  // se filtran las tuplas que tienen más de una dependencia en la lista
  val dups = deps.filter(_._2.size > 1)

  dups.foreach((t: (String, List[String])) => println(mkString(t._1, t._2)))

}
