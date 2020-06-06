package com.cmartin.learn

import com.cmartin.learn.Json4sResearch._
import org.json4s.JsonAST.{JDouble, JNothing, JValue}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
class Json4sResearchSpec extends AnyFlatSpec with Matchers {

  import Json4sResearchSpec._

  behavior of "Json4sResearch"

  it should "match a single xpath expression" in {
    val path = "timestamp"

    val result = isXpath(path)

    result shouldBe true
  }

  it should "match a simple xpath expression" in {
    val path = "features.battery.voltage"

    val result = isXpath(path)

    result shouldBe true
  }

  it should "parse a json message" in {
    val result: JValue = parse(inputMessageJson)

    result shouldBe an[JValue]
  }

  it should "get an existing key in a json message" in {
    val json           = parse(inputMessageJson)
    val result: JValue = getKey("features.location.coordinate.lat", json)

    result shouldBe JDouble(-12.21099)
  }

  it should "return the JNothing value for a non-existing key in a json message" in {
    val json           = parse(inputMessageJson)
    val result: JValue = getKey("features.xxx.lat", json)

    result shouldBe JNothing
  }

  it should "TODO return the JNothing value for a non-existing key in a json message" in {
    val json = parse(inputMessageJson)
    a[RuntimeException] shouldBe thrownBy(getKey("", json))
  }

}

object Json4sResearchSpec {

  val inputMessageJson: String =
    """
      |{
      |  "id":1234453722394796032,
      |  "timestamp":"2020-03-02T12:21:15.000Z",
      |  "server":{
      |    "timestamp":"2020-03-02T12:21:17.110Z"
      |  },
      |  "attributes":{
      |    "manufacturer":"Calamp",
      |    "model":"Calamp32Bits",
      |    "identifier":"1232064621"
      |  },
      |  "features":{
      |    "battery":{
      |      "voltage":13600.0
      |    },
      |    "engine":{
      |      "running":{
      |        "status":true
      |      }
      |    },
      |    "location":{
      |      "coordinate":{
      |        "lat":-12.21099,
      |        "lng":-76.961865
      |      },
      |      "altitude":8.916,
      |      "speed":38,
      |      "course":240
      |    },
      |    "vehicle":{
      |      "ignition":{
      |        "status":true
      |      }
      |    }
      |  }
      |}
      |""".stripMargin
}
