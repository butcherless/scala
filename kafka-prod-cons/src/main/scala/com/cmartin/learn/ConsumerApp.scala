package com.cmartin.learn

import java.time.Duration
import java.util

import com.cmartin.learn.Configuration.UuidConsumer
import com.cmartin.learn.common.ComponentLogging

import scala.jdk.CollectionConverters._

object ConsumerApp extends App with ComponentLogging {
  val loopCount = 10

  UuidConsumer.consumer.subscribe(
    util.Collections.singletonList(UuidConsumer.kafkaTopic)
  )

  for (c <- 1 to loopCount) {
    val records = UuidConsumer.consumer.poll(Duration.ofMillis(500))
    log.info(s"loop count=$c, records: ${records.count}")
    for (record <- records.asScala) {
      log.debug(s"kafka message: $record")
      log.info(s"kafka message: ${record.value}")
      UuidConsumer.consumer.commitSync
    }
  }

  UuidConsumer.consumer.close()
}
