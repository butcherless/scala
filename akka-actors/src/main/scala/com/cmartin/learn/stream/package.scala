package com.cmartin.learn

import akka.stream.scaladsl.Source
import scala.concurrent.duration._

package object stream {

  def buildIntSource(n: Int) = {
    Source(1 to n).throttle(1,1 second)
  }
}
