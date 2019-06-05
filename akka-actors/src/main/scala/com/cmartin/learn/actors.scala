package com.cmartin.learn

import akka.actor.{ Actor, ActorLogging, ActorRef, PoisonPill }
import com.cmartin.learn.actors.Consumer.Success

package object actors {

  class Producer extends Actor with ActorLogging {
    override def receive: Receive = {
      case Consumer.Success(mod) => {
        log.info(s"module is: ${mod}")
      }
      case Consumer.Failure(e) => log.error(s"processing error: $e")

      case Producer.Request(n) =>
    }

    def requestSequence(consumer: ActorRef) = {
      consumer ! Producer.Module(22)
    }
  }

  // companion object
  object Producer {
    case class Request(n: Int)
    case class Module(n: Int)

    case object Stop

  }

  class Consumer(producer: ActorRef) extends Actor with ActorLogging {
    override def receive: Receive = {
      case Producer.Module(n) => {
        producer ! Success(n % 10)
        log.info(s"request module for number: ${n}")
      }
      case Producer.Stop => {
        log.info(s"shutdown actor $self")
        self ! PoisonPill
      }

    }
  }

  // companion object
  object Consumer {

    case class Success(n: Int)

    case class Failure(message: String)

  }

}
