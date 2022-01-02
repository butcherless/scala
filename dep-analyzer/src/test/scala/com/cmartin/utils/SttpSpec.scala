package com.cmartin.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime

class SttpSpec extends AnyFlatSpec with Matchers {
  val runtime = Runtime.default

  // TODO Sttp/Zio-2 implicit val backend = Runtime.default.unsafeRun(AsyncHttpClientZioBackend())

  behavior of "Sttp client"

  "Raw interpolator" should "build an encoded string" in {
    val group    = "dev.zio"
    val artifact = "zio_2.13"

    val filter = s"q=g:$group+AND+a:$artifact+AND+p:jar&rows=1&wt=json"
    val uri    = raw"https://search.maven.org/solrsearch/select?$filter"

    info(uri)
  }

  /*
  ignore should "make a post request" in {
    val postRequest = basicRequest
      .post(uri"http://httpbin.org/post")
      .body("dummy post body")

    info(postRequest.toCurl)

    val postResponse: Task[Response[Either[String, String]]] =
      postRequest.send()

    val bodyResult: URIO[Any, Either[Throwable, Response[Either[String, String]]]] =
      postResponse.either

    val result: Either[Throwable, Response[Either[String, String]]] =
      runtime.unsafeRun(bodyResult)

    result match {
      case Right(value) =>
        value.body match {
          case Right(value) =>
            info(value)
            value.contains("dummy post body") shouldBe true

          case Left(value) => fail("expected successful result")
        }
      case Left(value) => fail(s"expected successful result: $value")
    }
  }

  ignore should "make a GET request" in {
    val group = "dev.zio"
    val artifact = "zio_2.13"

    val filter = s"q=g:$group+AND+a:$artifact+AND+p:jar&rows=1&wt=json"
    val rawUri = raw"https://search.maven.org/solrsearch/select?$filter"

    val getRequest = basicRequest
      .get(uri"$rawUri")

    info(getRequest.toCurl)

    val getResponse = getRequest.send()
    val bodyResult = getResponse.either
    val result: Either[Throwable, Response[Either[String, String]]] =
      runtime.unsafeRun(bodyResult)

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
   */
}
