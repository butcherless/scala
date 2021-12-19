package com.cmartin.learn.actor

import com.typesafe.config.ConfigFactory

trait ApiConfiguration {
  case class ServerConfig(interface: String, port: Int)

  val serverConfig = ServerConfig(
    ConfigFactory.load().getString("server.interface"),
    ConfigFactory.load().getInt("server.port")
  )
}
