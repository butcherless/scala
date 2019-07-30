package com.cmartin.learn.actors

import akka.actor.{Actor, ActorLogging, Props}

object Definition {

  class DummyDispatcherActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case a: Int =>
        log.debug(s"Int received $a")
        // simulates processing, echo process
        delayUpTo(100)
        val result = a * 1
        sender() ! result
      case x@_ =>
        log.warning(s"Non-Int received $x")
    }
  }

  object DummyDispatcherActor {
    def props() = Props(new DummyDispatcherActor)
  }

}
