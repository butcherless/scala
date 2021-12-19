package com.cmartin.utils

//import com.cmartin.utils.DependencyLookoutApp.unsafeRun
import com.cmartin.utils.file.FileManager
import com.cmartin.utils.http.HttpManager
import com.cmartin.utils.logic.LogicManager

object ApplicationHelper {

  type Definitions =
    FileManager with LogicManager with HttpManager // TODO  with ConfigManager
  /*
  trait Modules
      extends FileManagerLive
      with LogicManagerLive
      with HttpManagerLive

  val modules: Modules = new Modules {
    override implicit val backend: SttpBackend[Task, Nothing, WebSocketHandler] =
      unsafeRun(AsyncHttpClientZioBackend())
  }
   */

}
