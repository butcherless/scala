package com.cmartin

import scala.beans.BeanProperty

case class ApplicationVersion(@BeanProperty version: String, @BeanProperty dateTime: String)
