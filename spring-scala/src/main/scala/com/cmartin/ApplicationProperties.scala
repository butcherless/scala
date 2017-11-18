package com.cmartin

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ApplicationProperties {

  @Value("${maxRandom}")
  val maxRandom: Int = 0

  @Value("${version}")
  val version: String = null
}
