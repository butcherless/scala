package com.cmartin.utils.config

import zio.config.ConfigDescriptor._
import zio.config.{ConfigSource, ReadError, read}

object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: String)

  def getAppConfigFromMap(map: Map[String, String]) = {

    // config descriptor
    val appConfig =
      (string("FILENAME") |@| string("EXCLUSIONS"))(AppConfig.apply, AppConfig.unapply)

    // IO effect
    val io: Either[ReadError[String], AppConfig] = read(appConfig from ConfigSource.fromMap(map))
  }

}
