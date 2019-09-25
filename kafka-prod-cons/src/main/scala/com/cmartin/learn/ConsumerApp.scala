package com.cmartin.learn

import java.time.Duration
import java.util

import com.cmartin.learn.Configuration.SimpleConsumer
import com.typesafe.scalalogging.Logger

import scala.jdk.CollectionConverters._

object UuidConsumer
  extends SimpleConsumer

object ConsumerApp
  extends App {

  val logger = Logger[ConsumerApp.type]
  val loopCount = 10

  UuidConsumer.consumer.subscribe(util.Collections.singletonList(UuidConsumer.kafkaTopic))

  for (c <- 1 to loopCount) {
    val records = UuidConsumer.consumer.poll(Duration.ofMillis(500))
    logger.info(s"loop count=$c, records: ${records.count}")
    for (record <- records.asScala) {
      logger.debug(s"kafka message: $record")
      logger.info(s"kafka message: ${record.value}")
      UuidConsumer.consumer.commitSync
    }
  }

  UuidConsumer.consumer.close()
}
