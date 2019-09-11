package com.cmartin.utils

import scala.util.matching.Regex

/*
  http get: http -v https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
 */

object DependencyAnalyzer
  extends App
    with ComponentLogging {

  import Logic._

  import scala.io.Source

  //https://search.maven.org/solrsearch/select\?q\=g:"com.typesafe.akka"%20AND%20a:"akka-actor_2.13"%20AND%20v:"2.5.25"%20AND%20p:"jar"\&rows\=1\&wt\=json
  val uri = raw"https://search.maven.org/solrsearch/select?q=g:com.typesafe.akka%20AND%20a:akka-actor_2.13%20AND%20v:2.5.25%20AND%20p:jar&rows=1&wt=json"

  val pattern = raw"(^[a-z][a-z0-9-_\.]+):([a-z0-9-_\.]+):([0-9A-Z-\.]+)".r

  val httpManager = HttpManager()

  val bufferedSource = Source.fromFile("/tmp/deps.log")

  for (line <- bufferedSource.getLines) {

    val matches = pattern.matches(line)

    log.debug(s"reading dep candidate: $line matches regex? $matches")

    if (matches) {
      val r1: Iterator[Regex.Match] = pattern.findAllMatchIn(line)
      val regexMatch: Regex.Match = r1.next()

      val dep = Dep(regexMatch.group(1), regexMatch.group(2), regexMatch.group(3))
      log.debug(s"searching version for: $dep")

      httpManager.getDependencies(dep)
    }

  }
  bufferedSource.close

}
