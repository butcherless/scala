package com.cmartin

import scala.beans.BeanProperty

/**
  * Created by cmartin on 02/12/2016.
  */
case class Person(@BeanProperty val id: Long,
                  @BeanProperty val firstName: String,
                  @BeanProperty val lastName: String,
                  @BeanProperty val email: String) extends java.io.Serializable {
  def this() {
    this(0, "", "", "")
  }
}
