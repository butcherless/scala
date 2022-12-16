package com.cmartin.utils

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.config.ConfigHelper._
import com.cmartin.utils.domain.{HttpManager, IOManager, LogicManager}
import com.cmartin.utils.file._
import com.cmartin.utils.domain.Model.DomainError
import zio._

/** Helper application for keeping a project's dependencies up to date. Using
  * ZIO effect ZIO[R, E, A]
  *   - R: application services / dependencies
  *   - E: error in case of failure
  *   - A: returned value
  *
  * Examples:
  *   - R = { ConfigManger, FileManager, LogicManager, HttpManager }
  *   - E: ConnectionError (ADT member)
  *   - A: Either[Exception, Results]
  *
  * `R` arameter is similar to dependency injection and the `provide` function
  * can be thought of as `inject`.
  */
object DependencyLookoutApp
    extends ZIOAppDefault {

  override val bootstrap = ConfigHelper.loggingLayer

  override def run: IO[DomainError, Unit] = {

    // TODO resolve error channel type, actual Object
    val logicProgram =
      for {
        _           <- printBanner("Dep Lookout")
        config      <- readFromEnv()
        startTime   <- getMillis()
        lines       <- IOManager.getLinesFromFile(config.filename)
        parsedLines <- LogicManager.parseLines(lines) @@ iterablePairLog("parsingErrors")
        _           <- LogicManager.calculateValidRate(lines.size, parsedLines.successList.size) @@
                         genericLog("valid rate of dependencies")
        finalDeps   <- LogicManager.excludeFromList(parsedLines.successList, config.exclusions)
        results     <- HttpManager.checkDependencies(finalDeps)
        // TODO process errors
        _           <- IOManager.logPairCollection(results.gavList) @@ iterableLog("updated dependencies")
        _           <- IOManager.logWrongDependencies(results.errors)
        _           <- calcElapsedMillis(startTime) @@ genericLog("processing time")
      } yield 0

    // main program
    logicProgram
      .tapError(e => ZIO.logError(s"application error: $e"))
      .mapError(_ => 1)
      .fold(toExitCode, toExitCode)
      .flatMap(exit)
      .provide(applicationLayer)
  }

}
