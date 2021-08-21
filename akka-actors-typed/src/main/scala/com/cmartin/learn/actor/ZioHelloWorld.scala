package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import zio._

object ZioHelloWorld extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    Managed
      .acquireReleaseWith(Task(ActorSystem(HelloWorldMain(), "hello")))(
        system => Task(system.terminate()).ignore
      )
      .use(system =>
        for {
          _ <- Task(system ! HelloWorldMain.Start("World"))
          _ <- Task(system ! HelloWorldMain.Start("Akka"))
        } yield ExitCode.success
      )
      .catchAll(e => UIO(println(e)) *> UIO(ExitCode.failure))
}
