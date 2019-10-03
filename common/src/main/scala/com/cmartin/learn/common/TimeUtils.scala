package com.cmartin.learn.common

object TimeUtils {
  def doDelay(delay: Int) = {
    val timeout = System.currentTimeMillis() + delay

    while (System.currentTimeMillis() < timeout) {
      Thread.sleep(5)
    }
  }

}
