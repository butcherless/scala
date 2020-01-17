package com.cmartin.utils.config

import zio.IO
import zio.config.ConfigDescriptor._
import zio.config._

object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: String)

  def getAppConfigFromMap(): IO[ReadErrorsVector[String, String], AppConfig] = {
    // source
    val mapSource = Map(
      "FILENAME"   -> "dependencies.data",
      "EXCLUSIONS" -> "dep-exclusion-1"
    )

    // config descriptor
    val appConfig: ConfigDescriptor[String, String, AppConfig] =
      (string("FILENAME") |@| string("EXCLUSIONS"))(AppConfig.apply, AppConfig.unapply)

    // IO effect
    read(appConfig from ConfigSource.fromMap(mapSource))
  }

}
