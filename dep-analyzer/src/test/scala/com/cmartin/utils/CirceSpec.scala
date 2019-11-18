package com.cmartin.utils

import com.cmartin.utils.Logic.Dep
import com.cmartin.utils.http.HttpManager.Document
import io.circe
import io.circe.CursorOp.DownField
import io.circe.DecodingFailure
import io.circe.generic.auto._
import io.circe.parser._
import org.scalatest.{FlatSpec, Matchers}

class CirceSpec extends FlatSpec with Matchers {

  import CirceSpec._

  "Parser response" should "return a dependency" in {
    // given a json response with a dependency
    val expectedDep = Dep("com.softwaremill.sttp", "async-http-client-backend-zio_2.13", "1.7.1")

    // when
    val depEither = parseResponse(searchOkResponse)

    // then
    depEither.contains(expectedDep) shouldBe true
  }

  it should "return an error" in {
    // given a json response without a dependency

    // when
    val depEither: Either[circe.Error, Dep] = parseResponse(searchKoResponse)

    // then
    depEither should be(Symbol("left"))
    depEither.swap.map { error =>
      assert(error.isInstanceOf[DecodingFailure])
    }
  }

}

object CirceSpec {

  def parseResponse(response: String): Either[circe.Error, Dep] = {
    val opsResult: Either[circe.Error, Dep] = for {
      json <- parse(response)
      cursor = json.hcursor
      count <- cursor.downField("response").get[Int]("numFound")
      doc <- {
        if (count > 0)
          cursor.downField("response").downField("docs").downArray.as[Document]
        else
          Left(DecodingFailure("no elements", List(DownField("response"), DownField("numFound"))))
      }
    } yield Dep(doc.g, doc.a, doc.latestVersion)

    opsResult
  }

  val searchOkResponse =
    """
      |{
      |  "responseHeader": {
      |    "status": 0,
      |    "QTime": 0,
      |    "params": {
      |      "q": "g:com.softwaremill.sttp AND a:async-http-client-backend-zio_2.13 AND p:jar",
      |      "core": "",
      |      "indent": "off",
      |      "spellcheck": "true",
      |      "fl": "id,g,a,latestVersion,p,ec,repositoryId,text,timestamp,versionCount",
      |      "start": "",
      |      "sort": "score desc,timestamp desc,g asc,a asc",
      |      "spellcheck.count": "5",
      |      "rows": "1",
      |      "wt": "json",
      |      "version": "2.2"
      |    }
      |  },
      |  "response": {
      |    "numFound": 1,
      |    "start": 0,
      |    "docs": [
      |      {
      |        "id": "com.softwaremill.sttp:async-http-client-backend-zio_2.13",
      |        "g": "com.softwaremill.sttp",
      |        "a": "async-http-client-backend-zio_2.13",
      |        "latestVersion": "1.7.1",
      |        "repositoryId": "central",
      |        "p": "jar",
      |        "timestamp": 1570445611000,
      |        "versionCount": 8,
      |        "text": [
      |          "com.softwaremill.sttp",
      |          "async-http-client-backend-zio_2.13",
      |          "-sources.jar",
      |          "-javadoc.jar",
      |          ".jar",
      |          ".pom"
      |        ],
      |        "ec": [
      |          "-sources.jar",
      |          "-javadoc.jar",
      |          ".jar",
      |          ".pom"
      |        ]
      |      }
      |    ]
      |  },
      |  "spellcheck": {
      |    "suggestions": []
      |  }
      |}
      |""".stripMargin

  val searchKoResponse =
    """
      |{
      |  "responseHeader": {
      |    "status": 0,
      |    "QTime": 1,
      |    "params": {
      |      "q": "g:com.typesafe AND a:config AND p:jar",
      |      "core": "",
      |      "indent": "off",
      |      "spellcheck": "true",
      |      "fl": "id,g,a,latestVersion,p,ec,repositoryId,text,timestamp,versionCount",
      |      "start": "",
      |      "sort": "score desc,timestamp desc,g asc,a asc",
      |      "spellcheck.count": "5",
      |      "rows": "1",
      |      "wt": "json",
      |      "version": "2.2"
      |    }
      |  },
      |  "response": {
      |    "numFound": 0,
      |    "start": 0,
      |    "docs": []
      |  },
      |  "spellcheck": {
      |    "suggestions": []
      |  }
      |}
      |""".stripMargin
}
