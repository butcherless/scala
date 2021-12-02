package com.cmartin.learn.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import zio.UIO

/** Proprociona un logger al componente que lo utilice (mixin)
  */
trait ComponentLogging {
  val log: Logger = LoggerFactory.getLogger(getClass)

  def logDebug(message: String): UIO[Unit] =
    UIO.succeed(log.debug(message))

  def logInfo(message: String): UIO[Unit] =
    UIO.succeed(log.info(message))

  def logError(message: String): UIO[Unit] =
    UIO.succeed(log.error(message))
}
