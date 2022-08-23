package com.cmartin.utils.poc

import zio.config.ConfigDescriptor._
import zio.config._
import zio.{IO, ZIO, ZIOAppDefault}

object ZioEnvConfigApp
    extends ZIOAppDefault {

  object AppConfiguration {

    final case class EnvConfig(filename: String, exclusions: List[String])

    val envConfigDescriptor: ConfigDescriptor[EnvConfig] =
      string("FILENAME")
        .zip(list("EXCLUSIONS")(string))
        .to[EnvConfig]

    def readFromEnv(): IO[ReadError[String], EnvConfig] =
      read(
        envConfigDescriptor.from(
          ConfigSource.fromSystemEnv(valueDelimiter = Some(','))
        )
      )
  }

  override def run =
    (
      for {
        _         <- ZIO.succeed(System.setProperty("FILENAME", "don-pimpón-y-su-paquetón"))
        _         <- ZIO.logDebug("debug: loading environment variables")
        _         <- ZIO.logInfo("info: loading environment variables")
        envConfig <- AppConfiguration.readFromEnv()
        _         <- ZIO.logInfo(s"env config: $envConfig")
      } yield ()
    ).catchAllCause(cause =>
      ZIO.logError(s"${cause.prettyPrint}")
        .exitCode
    )
}
