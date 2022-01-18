package com.cmartin.utils.config

import zio.config.ConfigDescriptor._
import zio.config._
object ConfigHelper {

  final case class AppConfig(filename: String, exclusions: String)

  // config descriptor
  val configDescriptor: ConfigDescriptor[AppConfig] =
    string("FILENAME")
      .zip(string("EXCLUSIONS"))
      .to[AppConfig]

  final case class AppConfig2(filename: String, exclusions: List[String])
  val configDescriptor2: ConfigDescriptor[AppConfig2] =
    string("filename")
      .zip(list("exclusions")(string))
      .to[AppConfig2]

}
