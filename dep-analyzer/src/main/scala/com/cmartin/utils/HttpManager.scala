package com.cmartin.utils

import com.cmartin.utils.HttpManager.{Document, HttpBinResponse}
import com.cmartin.utils.Logic.Dep
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp._
import com.softwaremill.sttp.json4s._
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HttpManager
  extends ComponentLogging {

  implicit val serialization = org.json4s.native.Serialization
  implicit val formats = DefaultFormats

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

  def getDependencies(dep: Dep) = {
    val uri = raw"https://search.maven.org/solrsearch/select?q=g:${dep.group}%20AND%20a:${dep.artifact}%20AND%20p:jar&rows=1&wt=json"

    val request = sttp.get(uri"$uri")

    implicit val backend = HttpURLConnectionBackend()
    val response = request.send()

    val headers = response.headers.mkString(",\n")
    log.debug(s"headers $headers")

    // response.unsafeBody: by default read into a String
    val bodyString = response.unsafeBody
    log.debug(s"response body: $bodyString")

    val responseJson = parse(bodyString) \ "response" \ "docs"
    log.debug(s"doc list: $responseJson")
    val docList = responseJson.extract[Seq[Document]]

    docList.headOption match {
      case Some(remoteDep) =>
        val isNew = remoteDep.latestVersion != dep.version
        if (isNew) {
          log.info(s"dependency: ${remoteDep.g}:${remoteDep.a}:${remoteDep.latestVersion} <-> ${dep.version} is new? $isNew")
        }
      case None =>
        log.debug(s"dependency not found: $dep")
    }
  }
}

object HttpManager {

  case class Document(
                       id: String,
                       g: String,
                       a: String,
                       latestVersion: String,
                       p: String,
                       timestamp: Long
                     )

  case class Response(
                       numFound: Int,
                       start: Int,
                       docs: Seq[Document]
                     )

  def apply(): HttpManager = new HttpManager()

  case class HttpBinResponse(origin: String, headers: Map[String, String])

}