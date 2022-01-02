package com.cmartin.utils

import org.slf4j.MDC
import zio.{FiberId, FiberRef, LogLevel, LogSpan, ZLogger, ZTraceElement}

/* TODO
   - implement log spans
 */
object Slf4jLogger {

  val defaultSlf4j: ZLogger[String, Unit]                                   = (
      trace: ZTraceElement,
      fiberId: FiberId,
      logLevel: LogLevel,
      message0: () => String,
      context: Map[FiberRef.Runtime[_], AnyRef],
      spans0: List[LogSpan],
      location: ZTraceElement
  ) => {

    val _              = context
    val _              = spans0
    val _              = location
    val message        = message0()
    val (loc, _, line) = trace match {
      case ZTraceElement(location, file, line) =>
        (location, file, line)
      case _                                   => ("default-location", "", 0)
    }
    val logger         = org.slf4j.LoggerFactory.getLogger(loc)

    MDC.put("fiber", fiberId.threadName)
    MDC.put("line", line.toString)
    logLevel match {
      case LogLevel.All     => logger.trace(message)
      case LogLevel.Runtime => logger.trace(message)
      case LogLevel.Debug   => logger.debug(message)
      case LogLevel.Info    => logger.info(message)
      case LogLevel.Warning => logger.warn(message)
      case LogLevel.Error   => logger.error(message)
      case LogLevel.Fatal   => logger.error(message)
      case _                =>
    }

  }

  private def appendQuoted(label: String, sb: StringBuilder): StringBuilder = {
    if (label.indexOf(" ") < 0) sb.append(label)
    else sb.append("\"").append(label).append("\"")
    sb
  }

}
