package com.cmartin.utils

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.config.ConfigHelper._
import com.cmartin.utils.file._
import com.cmartin.utils.http.HttpManager
import com.cmartin.utils.logic.Common.{calcElapsedMillis, getMillis}
import com.cmartin.utils.logic.LogicManager
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

  /* for ZIO 2 RC3 version
     Disable default runtime logger and then apply custom runtime config aspect
     val rta = RuntimeConfigAspect(_ => runtime.runtimeConfig.copy(logger = ZLogger.none)) >>> aspectOne
     override def hook: RuntimeConfigAspect = rta
   */

  override val bootstrap = ConfigHelper.logAspect

  override def run = {

    // TODO resolve error channel type, actual Object
    def logicProgram(filename: String) =
      (
        for {
          _           <- printBanner("Dep Lookout")
          config      <- ConfigHelper.readFromFile(filename)
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
        } yield ()
      ).provide(applicationLayer)

    // main program
    for {
      args <- getArgs
      _    <- ZIO.when(args.isEmpty) {
                Console.printLine(s"Please, supply hocon config file, for example: /tmp/application-config.hocon") *>
                  ZIO.fail("empty command line arguments")
              }
      _    <- logicProgram(args.head)
    } yield ()
  }

}
