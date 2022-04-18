package com.cmartin.utils.file

import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.{default => runtime}

class LogicManagerITSpec
    extends AnyFlatSpec
    with Matchers {

  behavior of "LogicManager"

  it should "successfully retrieve the dependencies from a file" in {
    val filename = "dep-analyzer/src/it/resources/deps-1.txt"
    val program  = for {
      lines       <- IOManager(_.getLinesFromFile(filename))
      parsedLines <- LogicManager(_.parseLines(lines))
    } yield (parsedLines.failedList, parsedLines.successList, lines.size)

    val (errors, dependencies, lineCount) = runtime.unsafeRun(
      program.provide(FileManager.layer ++ LogicManagerLive.layer)
    )

    errors shouldBe empty
    dependencies should have size lineCount
  }

  it should "retrieve two collections of dependencies with successes and failures" in {
    val filename = "dep-analyzer/src/it/resources/deps-2.txt"
    val program  = for {
      lines       <- IOManager(_.getLinesFromFile(filename))
      parsedLines <- LogicManager(_.parseLines(lines))
    } yield (parsedLines.failedList.size, parsedLines.successList, lines.size)

    val (errors, dependencies, lineCount) = runtime.unsafeRun(
      program.provide(FileManager.layer ++ LogicManagerLive.layer)
    )

    errors shouldBe 2
    dependencies should have size (lineCount - errors)
  }

}

object LogicManagerITSpec {
  val dependencies = List(
    "com.cmartin.learn:fp-in-scala_2.13:1.0.0-SNAPSHOT",
    "org.scalaz:scalaz-core_2.13:7.2.28",
    "org.typelevel:cats-core_2.13:2.0.0",
    "ch.qos.logback:logback-classic:1.2.3",
    "ch.qos.logback:logback-core:1.2.3"
  )
}
