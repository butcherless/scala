package com.cmartin.utils

import com.cmartin.utils.config.ConfigHelper
import com.cmartin.utils.config.ConfigHelper._
import com.cmartin.utils.file._
import com.cmartin.utils.http.HttpManager
import com.cmartin.utils.logic.Common._
import com.cmartin.utils.logic.LogicManager
import zio._
import zio.config._

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

  override def hook: RuntimeConfigAspect = ConfigHelper.logAspect

  override def run = {

    // TODO resolve error channel type, actual Object
    def logicProgram(filename: String) =
      (
        for {
          _              <- printBanner("Dep Lookout")
          config         <- getConfig[ConfigHelper.AppConfig]
          startTime      <- getMillis()
          lines          <- IOManager(_.getLinesFromFile(config.filename))
          (_, validDeps) <- LogicManager(_.parseLines(lines)) @@ iterablePairLog("parsingErrors")
          _              <- LogicManager(_.calculateValidRate(lines.size, validDeps.size)) @@
                              genericLog("valid rate of dependencies")
          finalDeps      <- LogicManager(_.excludeFromList(validDeps, config.exclusions))
          results        <- HttpManager(_.checkDependencies(finalDeps))
          // TODO process errors
          _              <- IOManager(_.logPairCollection(results.gavList)) @@ iterableLog("updated dependencies")
          _              <- IOManager(_.logWrongDependencies(results.errors))
          _              <- calcElapsedMillis(startTime) @@ genericLog("processing time")
        } yield ()
      ).provide(
        buildLayerFromFile(filename),
        applicationLayer
      )

    // main program
    for {
      args <- getArgs
      _    <- ZIO.when(args.isEmpty) {
                Console.printLine(s"Please, supply hocon config file, for example: /tmp/application-config.hocon") *>
                  IO.fail("empty command line arguments")
              }
      _    <- logicProgram(args.head)
    } yield ()
  }

}
