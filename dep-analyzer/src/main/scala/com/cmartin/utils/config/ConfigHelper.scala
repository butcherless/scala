package com.cmartin.utils.config

import zio.config.ConfigDescriptor._
import zio.config.{ConfigSource, ReadError, read}
import zio.config.ConfigDescriptor

object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: String)

  // config descriptor
  val configDescriptor: ConfigDescriptor[AppConfig] =
    (
      string("FILENAME") |@| string("EXCLUSIONS")
    )(
      AppConfig.apply,
      AppConfig.unapply
    )

  def getConfigFromMap(map: Map[String, String]): Either[ReadError[String], AppConfig] = {
    read(
      configDescriptor.from(ConfigSource.fromMap(map))
    )
  }

}
