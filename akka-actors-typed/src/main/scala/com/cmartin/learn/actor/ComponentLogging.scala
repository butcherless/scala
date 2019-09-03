package com.cmartin.learn.actor

import org.slf4j.LoggerFactory

/**
  * Proprociona un logger al componente que lo utilice (mixin)
  */
trait ComponentLogging {
  val log = LoggerFactory.getLogger(getClass)
}
