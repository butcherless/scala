package com.cmartin.utils.console

import zio.{DefaultRuntime, Runtime, Task, ZIO}

trait DatabaseLive extends Database {
  val database = new Database.Service {
    override def lookupUser(uid: String): Task[UserProfile] =
      Task(UserProfile("admin"))
  }
}

object DatabaseLive extends DatabaseLive

object DB {
  def lookupUser(uid: String): ZIO[Database, Throwable, UserProfile] =
    ZIO.accessM(_.database lookupUser uid)
}

object Another {
  def f1() : Task[String] = ???

  def foo() = {
    f1().provide(DatabaseLive)
  }

  val runtime = new DefaultRuntime{}

  runtime.unsafeRun(foo)
}