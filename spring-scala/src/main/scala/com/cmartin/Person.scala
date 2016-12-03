package com.cmartin

/**
  * Created by cmartin on 02/12/2016.
  */
case class Person(id: Long, firstName: String, lastName: String, email: String) {

  override def toString: String = {
    "Person[id=%s, firstName=%s, lastName=%s, email=%s]".format(id, firstName, lastName, email)
  }

}
