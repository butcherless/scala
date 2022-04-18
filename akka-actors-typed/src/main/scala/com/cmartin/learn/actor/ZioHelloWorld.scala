package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import zio._

object ZioHelloWorld extends ZIOAppDefault {

  def acquire()                       = Task.attempt(ActorSystem(HelloWorldMain(), "hello"))
  def release(system: ActorSystem[_]) = Task.attempt(system.terminate()).ignore

  def run =
    ZIO.acquireRelease(acquire())(release)
      .flatMap(system =>
        for {
          _ <- Task.attempt(system ! HelloWorldMain.Start("World"))
          _ <- Task.attempt(system ! HelloWorldMain.Start("Akka"))
        } yield ExitCode.success
      )
      .catchAll(e => UIO.succeed(ExitCode.failure))

}
