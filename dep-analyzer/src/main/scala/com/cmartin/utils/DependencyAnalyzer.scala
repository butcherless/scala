package com.cmartin.utils

object DependencyAnalyzer extends App {

  import com.cmartin.utils.Logic.{ getDependency, mkString }

  import scala.io.Source

  // check command line args
  if (args.isEmpty) System.exit(1)

  // filename first arg
  val bufferedSource = Source.fromFile(args.head)

  for (line <- bufferedSource.getLines) {

    val dep = getDependency(line)
    //println(s"dependency $dep matched at line: $line")
    val result = DependencyRepository.addDependency(dep)
    //println(s"added dep: $line result is $result")

  }
  bufferedSource.close

  // se filtran las tuplas que tienen más de una dependencia en la lista

  val dups = DependencyRepository.getSetByVersionCountGreaterThan(1)

  dups.foreach(e => println(mkString(e._1, e._2)))

}
