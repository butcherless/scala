package com.cmartin.learn

import java.util.UUID

import com.cmartin.learn.Configuration.SimpleProducer
import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.ProducerRecord

import scala.util.Random

object UuidProducer
  extends SimpleProducer

object ProducerApp
  extends App {

  val logger = Logger[ProducerApp.type]

  def buildRecord(m: String): ProducerRecord[String, String] = {
    new ProducerRecord[String, String](
      UuidProducer.kafkaTopic,
      "myKey",
      s"{message: $m, id: ${UUID.randomUUID()}}"
    )
  }

  val messageCount = 1000

  for (i <- 1 to messageCount) {
    val randomString = Random.alphanumeric.take(64).mkString
    UuidProducer.producer.send(buildRecord(s"$randomString[$i]"))
    logger.info(s"message $i sent")
  }

  UuidProducer.producer.close()

}
