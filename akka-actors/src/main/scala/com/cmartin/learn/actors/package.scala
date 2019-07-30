package com.cmartin.learn

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}

import scala.util.Random

package object actors {

  class NumberProcessor(module: Int) extends Actor with ActorLogging {

    import NumberProcessor._

    override def receive: Receive = {
      case Request(number) =>
        log.info(s"processing number: $number")
        if (number % 10 == module) {
          delay(250)
          sender ! Accepted
        }
        else {
          delay(50)
          sender ! Rejected
        }
      case message =>
        log.info(s"unable to process $message")
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

  class RequestDispatcher(processors: Seq[ActorRef]) extends Actor with ActorLogging {

    import NumberProcessor._

    override def receive: Receive = {
      case Request(number) => log.debug(s"dispatcher: message received with number [$number]")
      case Init => processors(0) ! Request(215)
      case Gromenauer => processors(1) ! Request(5)
      case Accepted => log.info("dispatcher: message accepted")
      case Rejected => log.info("dispatcher: message rejected")
      case Unknown =>
        log.info("dispatcher: message unknown")
        sender ! Request(0)
    }
  }

  object RequestDispatcher {
    def props(processors: Seq[ActorRef]) = Props(new RequestDispatcher(processors))
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

    import Consumer._

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


  /**
    * Sleeps the current thread for the milliseconds given as parameter
    *
    * @param ms milliseconds
    */
  def delay(ms: Long): Unit = {
    Thread.sleep(ms)
  }

  /**
    * Sleeps the current thread for a number of milliseconds between 5 and the
    * given parameter
    *
    * @param ms milliseconds
    */
  def delayUpTo(ms: Long): Unit = {
    val random = Random.nextLong(ms)
    val result = if (random < 5) 5L else random

    Thread.sleep(result)
  }

  /**
    * Generates a random number between 0 and 9
    *
    * @return the random number
    */
  def randomBetween0And9(): Int = Random.nextInt(10)

}
