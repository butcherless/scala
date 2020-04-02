package com.cmartin.utils.config

import zio.config.ConfigDescriptor._
import zio.config.{ConfigDescriptor, ConfigSource, read}

object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: String)

  def getAppConfigFromMap(map: Map[String, String]) = {

    // config descriptor
    val appConfig: ConfigDescriptor[String, String, AppConfig] =
      (string("FILENAME") |@| string("EXCLUSIONS"))(AppConfig.apply, AppConfig.unapply)

    // IO effect
    read(appConfig from ConfigSource.fromMap(map))
  }

}
