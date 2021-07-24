package com.cmartin.learn

package object model {

  /** Created by cmartin on 26/12/2016.
    */
  case class Person(
      name: String,
      firstName: String,
      age: ObfuscatedInt,
      id: String,
      password: ObfuscatedString
  )

  case class ObfuscatedString(s: String) {
    override def toString(): String = ("*" * s.length).take(8)
  }

  case class ObfuscatedInt(x: Int) {
    override def toString(): String = "*" * x.toString.length
  }

  object Constants {
    val name = "Carlos"
    val firstName = "Martin"
    val age = 49
    val id = "carlos.martin"
    val password = "dummy-password"
  }

}
