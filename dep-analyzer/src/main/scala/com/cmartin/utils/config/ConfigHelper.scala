package com.cmartin.utils.config

import com.cmartin.utils.file.{FileManager, IOManager}
import com.cmartin.utils.http.{HttpManager, ZioHttpManager}
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import com.cmartin.utils.model.Domain.{ConfigError, WebClientError}
import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{FIGureOps, Figlet4s, OptionsBuilderOps}
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.config.ConfigDescriptor._
import zio.config._
import zio.config.typesafe._
import zio.logging.backend.SLF4J
import zio.{Clock, IO, Layer, Runtime, Task, UIO, ZIO, ZIOAspect, ZLayer}

object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: List[String])

  def printBanner(message: String): UIO[Unit] =
    ZIO.succeed(
      Figlet4s.builder()
        .withInternalFont("doom")
        .withMaxWidth(120)
        .withHorizontalLayout(HorizontalLayout.HorizontalFitting)
        .render(message)
        .print()
    )

  /*
       A P P L I C A T I O N   P R O P E R T I E S
   */
  val configDescriptor: ConfigDescriptor[AppConfig] =
    string("filename")
      .zip(list("exclusions")(string))
      .to[AppConfig]

  // read config description from hocon file source
  def readFromFile(filename: String): IO[ConfigError, AppConfig] =
    read(
      configDescriptor.from(
        TypesafeConfigSource.fromHoconFilePath(filename)
      )
    ).mapError(e => ConfigError(e.toString()))

  def buildLayerFromFile(filename: String): Layer[ReadError[String], AppConfig] =
    ZConfig.fromHoconFilePath(filename, configDescriptor)

  def printConfig(): String =
    generateDocs(configDescriptor)
      .toTable
      .toGithubFlavouredMarkdown

  /*
     L O G G I N G
   */

  val logger = Runtime.addLogger(
    SLF4J.slf4jLogger(SLF4J.logFormatDefault, SLF4J.getLoggerName())
  )

  val loggerLayer = Runtime.removeDefaultLoggers >>> logger

  // loggers
  def genericLog[T](message: String) =
    ZIOAspect.loggedWith[T](a => s"$message: $a")

  def iterableLog(message: String) =
    ZIOAspect.loggedWith[Iterable[_]](i => s"$message:${i.mkString("\n", "\n", "")}")

  def iterablePairLog(message: String) =
    ZIOAspect.loggedWith[LogicManager.ParsedLines] { parsedLines =>
      parsedLines.failedList match {
        case Nil => s"$message: empty sequence of elements"
        case it  => s"$message:${it.mkString("\n", "\n", "")}"
      }
    }

  /*
     H T T P   C L I E N T   L A Y E R
   */

  val clientBackendLayer: ZLayer[Any, WebClientError, SttpBackend[Task, ZioStreams with WebSockets]] =
    ZLayer.scoped(HttpClientZioBackend.scoped())
      .mapError(th => WebClientError(th.getMessage))

  /*
     A P P L I C A T I O N   L A Y E R S
   */

  type ApplicationDependencies =
    Clock with IOManager with LogicManager with HttpManager with SttpBackend[Task, ZioStreams with WebSockets]

  val applicationLayer =
    ZLayer.make[ApplicationDependencies](
      ZLayer.succeed(Clock.ClockLive),
      FileManager.layer,
      LogicManagerLive.layer,
      ZioHttpManager.layer,
      clientBackendLayer,
      ZLayer.Debug.mermaid
    )
}
