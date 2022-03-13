package com.cmartin.utils.config

import com.cmartin.utils.file.{FileManager, FileManagerLive}
import com.cmartin.utils.http.{HttpManager, ZioHttpManager}
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import zio.config.ConfigDescriptor._
import zio.config._
import zio.config.typesafe._
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.{Clock, IO, Layer, LogLevel, RuntimeConfigAspect, ULayer, ZIOAspect, ZLayer}

object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: List[String])

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

  val logAspect: RuntimeConfigAspect =
    SLF4J.slf4j(
      logLevel = LogLevel.Debug,
      format = LogFormat.line
    )

  // loggers
  def genericLog[T](message: String) = ZIOAspect.loggedWith[T](a => s"$message: $a")

  def iterableLog(message: String) = ZIOAspect.loggedWith[Iterable[_]](i => s"$message:${i.mkString("\n", "\n", "")}")

  def iterablePairLog(message: String) = ZIOAspect.loggedWith[(Iterable[String], _)] { case (it, _) =>
    it match {
      case Nil => s"$message: empty sequence of elements"
      case _   => s"$message:${it.mkString("\n", "\n", "")}"
    }
  }

  type ApplicationDependencies =
    Clock with FileManager with HttpManager with LogicManager

  val applicationLayer: ULayer[ApplicationDependencies] =
    ZLayer.make[ApplicationDependencies](
      Clock.live,
      FileManagerLive.layer,
      ZioHttpManager.layer,
      LogicManagerLive.layer,
      ZLayer.Debug.mermaid
    )

}
