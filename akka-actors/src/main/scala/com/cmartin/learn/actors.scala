package com.cmartin.learn

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import com.cmartin.learn.actors.Consumer.Success

package object actors {

  class NumberProcessor(module: Int) extends Actor with ActorLogging {

    import NumberProcessor._

    override def receive: Receive = {
      case Request(number) =>
        log.info(s"processing number: $number")
        if (number % 10 == module) {
          sender ! Accepted
        }
        else {
          sender ! Rejected
        }
      case message =>
        log.info(s"unable to process de $message")
        sender ! Unknown
    }
  }

  object NumberProcessor {
    def props(module: Int) = Props(new NumberProcessor(module))

    case class Request(number: Int)

    case object Init
    case object Gromenauer

    case object Accepted

    case object Rejected

    case object Unknown

  }

  class RequestDispatcher(processor: ActorRef) extends Actor with ActorLogging {

    import NumberProcessor._

    override def receive: Receive = {
      case Init => processor ! Request(215)
      case Gromenauer => processor ! Gromenauer
      case Accepted => log.info("dispatcher: message accepted")
      case Rejected => log.info("dispatcher: message rejected")
      case Unknown =>
        log.info("dispatcher: message unknown")
        sender ! Request(0)
    }
  }

  object RequestDispatcher {
    def props(processor: ActorRef) = Props(new RequestDispatcher(processor))
  }













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
