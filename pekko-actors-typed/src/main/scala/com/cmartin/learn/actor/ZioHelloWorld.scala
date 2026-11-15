package com.cmartin.learn.actor

import org.apache.pekko.actor.typed.ActorSystem
import zio.*

object ZioHelloWorld extends ZIOAppDefault {

  def acquire()                       = ZIO.attempt(ActorSystem(HelloWorldMain(), "hello"))
  def release(system: ActorSystem[?]) = ZIO.attempt(system.terminate()).ignore

  def run =
    ZIO.acquireRelease(acquire())(release)
      .flatMap(system =>
        for {
          _ <- ZIO.attempt(system ! HelloWorldMain.Start("World"))
          _ <- ZIO.attempt(system ! HelloWorldMain.Start("Pekko"))
        } yield ExitCode.success
      )
      .catchAll(e => ZIO.succeed(ExitCode.failure))

}
