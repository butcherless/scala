package com.cmartin.utils

import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.zio.AsyncHttpClientZioBackend
import org.scalatest.{FlatSpec, Matchers}
import zio.{DefaultRuntime, Task, UIO, ZIO}

class SttpSpec extends FlatSpec with Matchers {

  val runtime          = new DefaultRuntime {}
  implicit val backend = AsyncHttpClientZioBackend()

  "Sttp client" should "make a post request" in {
    val postRequest = sttp
      .post(uri"http://httpbin.org/post")
      .body("dummy post body")

    info(postRequest.toCurl)

    val postResponse: Task[Response[String]] = postRequest.send()

    val bodyResult: UIO[Either[Throwable, Response[String]]] = postResponse.either

    val result: Either[Throwable, Response[String]] = runtime.unsafeRun(bodyResult)

    result match {
      case Right(value) =>
        value.body match {
          case Right(value) => value.contains("dummy post body") shouldBe true
          case Left(value)  => fail("expected successful result")
        }
      case Left(value) => fail(s"expected successful result: $value")
    }
  }

  "Raw interpolator" should "build an encoded string" in {
    val group    = "dev.zio"
    val artifact = "zio_2.13"

    val filter = s"q=g:$group+AND+a:$artifact+AND+p:jar&rows=1&wt=json"
    val uri    = raw"https://search.maven.org/solrsearch/select?$filter"

    info(uri)
  }

  "Sttp client" should "make a GET request" in {
    val group    = "dev.zio"
    val artifact = "zio_2.13"

    val filter = s"q=g:$group+AND+a:$artifact+AND+p:jar&rows=1&wt=json"
    val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"

    val getRequest = sttp
      .get(uri"$rawUri")

    info(getRequest.toCurl)

    val getResponse: ZIO[Any, Throwable, Response[String]]                 = getRequest.send()
    val bodyResult: ZIO[Any, Nothing, Either[Throwable, Response[String]]] = getResponse.either
    val result: Either[Throwable, Response[String]]                        = runtime.unsafeRun(bodyResult)

    result match {
      case Right(value) =>
        value.body match {
          case Right(value) =>
            info(value)
            value.contains("zio") shouldBe true
          case Left(_) => fail("expected successful result")
        }
      case Left(value) => fail(s"expected successful result: $value")
    }

  }

}
