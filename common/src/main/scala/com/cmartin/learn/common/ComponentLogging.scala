package com.cmartin.learn.common

import org.slf4j.{Logger, LoggerFactory}
import zio.{UIO, ZIO}

/** Proprociona un logger al componente que lo utilice (mixin)
  */
trait ComponentLogging {
  val log: Logger = LoggerFactory.getLogger(getClass)

  def logDebug(message: String): UIO[Unit] =
    ZIO.succeed(log.debug(message))

  def logInfo(message: String): UIO[Unit] =
    ZIO.succeed(log.info(message))

  def logError(message: String): UIO[Unit] =
    ZIO.succeed(log.error(message))
}
