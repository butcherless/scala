package com.cmartin.learn

import java.util.UUID

import org.apache.kafka.clients.producer.ProducerRecord

import scala.util.Random

object UuidProducer extends SimpleProducer

object ProducerApp extends App {

  def buildRecord(m: String): ProducerRecord[String, String] = {
    new ProducerRecord[String, String](
      UuidProducer.kafkaTopic,
      "myKey",
      s"{message: $m, id: ${UUID.randomUUID()}}"
    )
  }

  val messageCount = 100

  for (i <- 1 to messageCount) {
    val randomString = Random.alphanumeric.take(64).mkString
    UuidProducer.producer.send(buildRecord(s"$randomString[$i]"))
    Thread.sleep(messageCount / 10)
  }

  UuidProducer.producer.close

}
