//package com.cmartin.learn.actor
//
//import java.util.UUID
//
//import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
//import com.cmartin.learn.actor.ServiceActor.ServiceTellCommand
//import org.scalatest.flatspec.AnyFlatSpecLike
//
//class ServiceActorSpec extends ScalaTestWithActorTestKit with AnyFlatSpecLike {
//  "ServiceActor" should "WIP" in {
//    val probe        = createTestProbe[ServiceTellCommand]
//    val serviceActor = spawn(ServiceActor("actor-id"))
//
//    val id = UUID.randomUUID()
//    serviceActor ! ServiceTellCommand(id)
//
//    probe.expectNoMessage()
//  }
//}
