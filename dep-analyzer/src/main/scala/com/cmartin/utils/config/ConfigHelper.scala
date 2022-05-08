package com.cmartin.utils.config

import com.cmartin.utils.file.{FileManager, IOManager}
import com.cmartin.utils.http.{HttpManager, ZioHttpManager}
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{FIGureOps, Figlet4s, OptionsBuilderOps}
import sttp.capabilities
import sttp.capabilities.zio.ZioStreams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.{Clock, IO, Layer, LogLevel, RIO, Scope, Task, UIO, ULayer, ZIO, ZIOAspect, ZLayer}
import zio.config.ConfigDescriptor._
import zio.config._
import zio.config.typesafe._
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

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
  def readFromFile(filename: String): IO[ReadError[String], AppConfig] =
    read(
      configDescriptor.from(
        TypesafeConfigSource.fromHoconFilePath(filename)
      )
    )

  def buildLayerFromFile(filename: String): Layer[ReadError[String], AppConfig] =
    ZConfig.fromHoconFilePath(filename, configDescriptor)

  def printConfig(): String =
    generateDocs(configDescriptor)
      .toTable
      .toGithubFlavouredMarkdown

  /*
     L O G G I N G
   */

  val logAspect =
    SLF4J.slf4j(
      logLevel = LogLevel.Debug,
      format = LogFormat.line
    )

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
     H T T P   C L I E N T
   */

  type ClientBackend = SttpBackend[Task, ZioStreams with capabilities.WebSockets]
  val clientBackendLayer: ULayer[RIO[Scope, ClientBackend]] = ZLayer.succeed(HttpClientZioBackend.scoped())

  /*
     A P P L I C A T I O N   L A Y E R S
   */

  type ApplicationDependencies =
    Clock with IOManager with LogicManager with RIO[Scope, ClientBackend] with HttpManager

  val applicationLayer =
    ZLayer.make[ApplicationDependencies](
      Clock.live,
      FileManager.layer,
      LogicManagerLive.layer,
      clientBackendLayer,
      ZioHttpManager.layer,
      ZLayer.Debug.mermaid
    )
}
