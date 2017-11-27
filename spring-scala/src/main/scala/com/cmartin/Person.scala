package com.cmartin

import scala.beans.BeanProperty

/**
  * Created by cmartin on 02/12/2016.
  */
class Person
/*
(@BeanProperty id: Long,
 @BeanProperty firstName: String,
 @BeanProperty lastName: String,
 @BeanProperty email: String)
 */
  extends java.io.Serializable {

  /*
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var firstName: String = _

  @BeanProperty
  var lastName: String = _

  @BeanProperty
  var email: String = _


  override def toString: String = {
    "Person[id=%s, firstName=%s, lastName=%s, email=%s]".format(id, firstName, lastName, email)
  }
*/

  private var id:Long=_

  def setId(id:Long) = this.id=id

  override def toString: String = {
    "Person[id=%s]".format(id) //, firstName, lastName, email)
  }
}
