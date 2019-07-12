package com.cmartin.learn

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

package object traverse {

  sealed trait ServiceResult

  object Ok extends ServiceResult

  object Ko extends ServiceResult

  val SERVICE_ERROR_MESSAGE = "Service response failed: 500"

  type ServiceResponse = Either[String, Int]

  val delayMax = 500

  def delay: Int = Random.nextDouble() * delayMax toInt


  def callRemoteServiceOption(expected: ServiceResult): Option[Int] = {
    expected match {
      case Ok => Some(200)
      case Ko => None
    }
  }

  def combineServiceResponses(serviceList: List[ServiceResult]): List[Option[Int]] = {
    serviceList.map(callRemoteServiceOption)
  }

  def callRemoteService(index: ServiceResult): Future[ServiceResponse] = {
    index match {
      case Ok => Future {
        val d = delay
        Thread.sleep(d)
        println(s"delay Ok: $d")
        Right(200)
      }

      case Ko => Future { // comes from .recover {...}
        val d = delay
        Thread.sleep(d)
        println(s"delay Ko: $d")
        throw new RuntimeException("Crash!!!")
      }.recoverWith {
        case _ => Future.successful(Left(SERVICE_ERROR_MESSAGE))
      }
    }
  }

  def composeServiceResponses(serviceList: List[ServiceResult]): Future[List[ServiceResponse]] = {
    Future.sequence(serviceList.map(callRemoteService))
  }

}
