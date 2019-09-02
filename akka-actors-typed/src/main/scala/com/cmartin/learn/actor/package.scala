package com.cmartin.learn

import java.util.UUID

package object actor {

  def getRequestId(): UUID = UUID.randomUUID()
}
