package com.cmartin

/**
  * Created by cmartin on 02/12/2016.
  */
class Person(id: Long, firstName: String, lastName: String, email: String) {

  def getId = id

  def getFirstName = firstName

  def getLastName = lastName

  def getEmail = email

  override def toString: String = {
    "Person[id=%s, firstName=%s, lastName=%s, email=%s]".format(id, firstName, lastName, email)
  }

}
