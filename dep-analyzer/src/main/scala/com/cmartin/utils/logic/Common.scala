package com.cmartin.utils.logic

import zio.{Clock, URIO}

import java.util.concurrent.TimeUnit

object Common {
  def getMillis(): URIO[Clock, Long] =
    Clock.currentTime(TimeUnit.MILLISECONDS)

  def calcElapsedMillis(start: Long): URIO[Clock, Long] =
    Clock.currentTime(TimeUnit.MILLISECONDS).map(_ - start)

}
