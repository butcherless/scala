package com.cmartin.learn

import java.util.{Date, UUID}

import com.cmartin.learn.AppHelper._
import com.cmartin.learn.Configuration.UuidProducer
import com.cmartin.learn.Domain.DummyMessage
import com.cmartin.learn.common.ComponentLogging
import org.apache.kafka.clients.producer.ProducerRecord

import scala.util.Random

object ProducerApp extends App with ComponentLogging {
  def buildRecord(id: Int, m: String): ProducerRecord[Int, String] = {
    new ProducerRecord[Int, String](
      UuidProducer.kafkaTopic,
      id,
      s"{message: $m, id: ${UUID.randomUUID()}}"
    )
  }

  def buildRecordFromDummyMessage(
      dm: DummyMessage
  ): ProducerRecord[Int, DummyMessage] = {
    new ProducerRecord[Int, DummyMessage](
      UuidProducer.kafkaTopic,
      dm.id,
      dm
    )
  }

  val messageCount = 1000

  for (i <- 1 to messageCount) {
    val deviceId: Int        = Random.nextInt(100)
    val randomString: String = Random.alphanumeric.take(64).mkString
    val timestamp: Long      = new Date().getTime()
    // UuidProducer.producer.send(buildRecord(deviceId, s"$randomString[$i]"))
    UuidProducer.producer.send(
      buildRecord(
        deviceId,
        buildDummyMessage(DummyMessage(i, randomString, timestamp, deviceId))
      )
    )
    log.info(s"message $i sent")
  }

  UuidProducer.producer.close()
}
