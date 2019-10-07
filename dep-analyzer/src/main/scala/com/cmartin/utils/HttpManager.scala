package com.cmartin.utils

import com.cmartin.learn.common.ComponentLogging
import com.cmartin.utils.HttpManager.Document
import com.cmartin.utils.Logic.Dep
import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.zio.AsyncHttpClientZioBackend
import io.circe.generic.auto._
import io.circe.parser._
import org.json4s._
import zio.{DefaultRuntime, IO, UIO, ZIO}

class HttpManager
  extends ComponentLogging {

  implicit val serialization = org.json4s.native.Serialization
  implicit val formats = DefaultFormats

  val runtime = new DefaultRuntime {}
  implicit val backend = AsyncHttpClientZioBackend()


  def checkDependencies(deps: Seq[Dep]): Unit = {
    val program = ZIO.foreachParN(4)(deps)(getDependency)
    val exec: Seq[Either[Throwable, (Dep, Dep)]] = runtime.unsafeRun(program)

    exec.foreach {
      case Left(value) => log.error(value.getMessage)
      case Right(value) =>
        val (local, remote) = value
        if (local != remote) log.info(value.toString())
    }

    backend.close()
  }


  def buildUri(dep: Dep): Uri = {
    val filter = s"q=g:${dep.group}+AND+a:${dep.artifact}+AND+p:jar&rows=1&wt=json"
    val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"
    uri"$rawUri"
  }


  def getDependency(dep: Dep): UIO[Either[Throwable, (Dep, Dep)]] = {
    IO.effect {
      val getRequest = sttp
        .get(buildUri(dep))

      val bodyResult: UIO[Either[Throwable, Response[String]]] =
        getRequest
          .send()
          .either
      val exec: Either[Throwable, Response[String]] = runtime.unsafeRun(bodyResult)

      exec match {
        case Right(response) => response.body match {
          case Right(body) =>
            log.debug(body)
            (dep, getDepFromResponse(body))

          case Left(errorMessage) =>
            log.info(s"expected successful result: $errorMessage")
            throw new RuntimeException(errorMessage)
        }
        case Left(exception) =>
          log.info(s"expected successful result: $exception")
          throw exception
      }

    }.either
  }

  def getDepFromResponse(body: String): Dep = {
    val depEither = for {
      json <- parse(body)
      _ <- {
        val cursor = json.hcursor
        cursor.downField("response").get[Int]("numFound").filterOrElse(_ == 1, 0)
      }
      doc <- {
        val cursor = json.hcursor
        cursor.downField("response").downField("docs").downArray.as[Document]
      }
    } yield Dep(doc.g, doc.a, doc.latestVersion)

    depEither.fold(e => throw new RuntimeException(s"unavailable dependency count: $e"), d => d)
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