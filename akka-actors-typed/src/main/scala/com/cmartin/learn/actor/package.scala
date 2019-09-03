package com.cmartin.learn

import java.util.UUID

package object actor {

  def getRequestId(): UUID = UUID.randomUUID()

  def isEven(n: Long): Boolean = (n % 2 == 0)
}
