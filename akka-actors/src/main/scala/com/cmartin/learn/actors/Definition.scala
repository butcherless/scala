package com.cmartin.learn.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.cmartin.learn.actors.Definition.DummyDispatcherActor.{Accepted, Rejected, Request, Unknown}

object Definition {

  class DummyDispatcherActor(workers: Seq[ActorRef]) extends Actor with ActorLogging {

    import DummyDispatcherActor._

    var map = Map.empty[String, RequestInfo]
    var processed: Int = 0
    var rejected: Int = 0

    override def receive: Receive = {

      case number: Int =>
        val id = generateId() // generates id for message
        val worker = randomBetween0And9() // picks a random worker
        val reqInfo = RequestInfo(sender(), id, number, worker)
        map += (id -> reqInfo) // add to pending message collection
        workers(worker) ! Request(number, id) // sends the message to the worker
        log.debug(s"[number, worker, id] = [$number, $worker, $id]")

      case Accepted(number, id) =>
        val reqInfo = map(id)
        map -= id // remove processed message
        processed += 1
        reqInfo.sender ! number // stream back-pressure
        log.info(s"Accepted: ${reqInfo.toShortString()}")

      case Rejected(number, id) => // TODO maximum number of hops
        val reqInfo = map(id)

        if (reqInfo.hops < HOP_MAX_COUNT) {
          val worker = randomBetween0And9() // picks the next worker
          map += (id -> reqInfo.copy(worker= worker, hops = reqInfo.hops + 1))
          workers(worker) ! Request(number, id) // sends the message to the worker
          log.debug(s"${Rejected(number, id)} => new worker[$worker]")
        } else { // self
          self ! Discarded(reqInfo.sender, number, id)
        }

      case Discarded(sender, number, id) =>
        map -= id // remove message
        rejected += 1
        sender ! number // stream back-pressure
        log.error(s"${Discarded}")

      case Stats =>
        val total = processed + rejected
        val okRate = 100.toDouble * processed / total
        val koRate = 100.toDouble * rejected / total
        log.info(s"Stats=[$total, $processed => $okRate, $rejected => $koRate]")
        sender() ! s"Stats=[$total, $processed, $rejected]"

      case Unknown =>
        log.warning("dispatcher: message unknown")

      case x@_ =>
        log.warning(s"Non-Int received $x")
    }
  }

  object DummyDispatcherActor {
    val HOP_MAX_COUNT = 20

    case class Request(number: Int, id: String)

    case class Accepted(number: Int, id: String)

    case class Discarded(sender: ActorRef, number: Int, id: String)

    case class Rejected(number: Int, id: String)

    case object Stats

    case object Unknown

    def props(workers: Seq[ActorRef]) = Props(new DummyDispatcherActor(workers))
  }

  class IntegerProcessor(modulo: Int) extends Actor with ActorLogging {

    override def receive: Receive = {
      case Request(number, id) =>
        log.debug(s"processing number: $number")
        if (number % 10 == modulo) {
          delayUpTo(50)
          sender ! Accepted(number, id)
        }
        else {
          delayUpTo(10)
          sender ! Rejected(number, id)
        }
      case message =>
        log.info(s"unable to process $message")
        sender ! Unknown
    }
  }

  object IntegerProcessor {
    def props(modulo: Int) = Props(new IntegerProcessor(modulo))
  }

  case class RequestInfo(sender: ActorRef, id: String, number: Int, worker: Int, hops: Int = 1) {
    def toShortString(): String = s"RequestInfo($id,$number,$worker,$hops)"
  }

}
