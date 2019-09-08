package com.cmartin.utils

import com.cmartin.utils.HttpManager.HttpBinResponse
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp._
import com.softwaremill.sttp.json4s._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HttpManager {

  implicit val serialization = org.json4s.native.Serialization

  def getDependencyDoc(uri: String) = {
    val request = sttp
      .get(uri"$uri")
      .response(asJson[HttpBinResponse])

    implicit val backend = AkkaHttpBackend()
    val response: Future[Response[HttpBinResponse]] = request.send()

    println(s"uri: $uri")

    for {
      r <- response
    } {
      println(s"Got response code: ${r.code}")
      println(r.body)
      backend.close()
    }
  }

}

object HttpManager {

  def apply(): HttpManager = new HttpManager()

  case class HttpBinResponse(origin: String, headers: Map[String, String])

}