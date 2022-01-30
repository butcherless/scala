package com.cmartin.utils.config

import zio.config.ConfigDescriptor._
import zio.config._
import zio.config.typesafe._
import zio.{IO, Layer}

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
}
