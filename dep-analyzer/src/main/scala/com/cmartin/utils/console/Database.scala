package com.cmartin.utils.console

import zio.Task

final case class UserProfile(name: String)

trait Database {
  val database: Database.Service
}

object Database {

  // The database service
  trait Service {
    def lookupUser(uid: String): Task[UserProfile]
  }

}