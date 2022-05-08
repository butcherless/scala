package com.cmartin.learn.actor

import akka.actor.typed.ActorSystem
import zio._

object ZioHelloWorld extends ZIOAppDefault {

  def acquire()                       = ZIO.attempt(ActorSystem(HelloWorldMain(), "hello"))
  def release(system: ActorSystem[_]) = ZIO.attempt(system.terminate()).ignore

  def run =
    ZIO.acquireRelease(acquire())(release)
      .flatMap(system =>
        for {
          _ <- ZIO.attempt(system ! HelloWorldMain.Start("World"))
          _ <- ZIO.attempt(system ! HelloWorldMain.Start("Akka"))
        } yield ExitCode.success
      )
      .catchAll(e => ZIO.succeed(ExitCode.failure))

}
