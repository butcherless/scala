package com.cmartin.utils.environment

import com.cmartin.learn.common.ComponentLogging
import zio.{App, UIO, ZIO}

object MainApp
  extends App
    with ComponentLogging {

  val filename = "dep-analyzer/src/main/resources/deps2.log"

  override def run(args: List[String]) = {

    //val db = ZIO.provide(FileManagerLive)




    log.info(s"Running MainApp")
    val program: ZIO[FileManager, Throwable, List[String]] =
      for {
        lines <- FileManagerHelper.getLinesFromFile("bla bla")
      } yield lines


    val programLive = program.provide(FileManagerLive)
    val result: Seq[String] =unsafeRun(programLive)

    UIO(0)
  }
}
