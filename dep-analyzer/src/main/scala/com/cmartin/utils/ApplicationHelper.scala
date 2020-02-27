package com.cmartin.utils

import com.cmartin.utils.DependencyLookoutApp.unsafeRun
import com.cmartin.utils.file.{FileManager, FileManagerLive}
import com.cmartin.utils.http.{HttpManager, HttpManagerLive}
import com.cmartin.utils.logic.{LogicManager, LogicManagerLive}
import sttp.client.SttpBackend
import sttp.client.asynchttpclient.WebSocketHandler
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import zio.Task

object ApplicationHelper {

  type Definitions = FileManager with LogicManager with HttpManager //TODO  with ConfigManager

  trait Modules extends FileManagerLive with LogicManagerLive with HttpManagerLive

  val modules: Modules = new Modules {
    override implicit val backend: SttpBackend[Task, Nothing, WebSocketHandler] =
      unsafeRun(AsyncHttpClientZioBackend())
  }

}
