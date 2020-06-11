package com.cmartin.learn

import com.cmartin.learn.Json4sResearch._
import org.json4s.JsonAST.{JDouble, JNothing, JObject, JValue}
import org.json4s.native.JsonMethods
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

  it should "parse a json document" in {
    val result: JValue = parse(inputMessageJson)

    result shouldBe an[JValue]
  }

  it should "get an existing key in a json document with a simple type value" in {
    val json           = parse(inputMessageJson)
    val result: JValue = getKey("features.location.coordinate.lat", json)

    result shouldBe JDouble(-12.21099)
  }

  it should "get an existing key in a json document with an object value" in {
    val json           = parse(inputMessageJson)
    val result: JValue = getKey("features.location.coordinate", json)

    result shouldBe JObject(
      ("lat", JDouble(-12.21099)) :: ("lng", JDouble(-76.961865)) :: Nil
    )
  }

  it should "return the JNothing value for a non-existing key in a json document" in {
    val json           = parse(inputMessageJson)
    val result: JValue = getKey("features.xxx.lat", json)

    result shouldBe JNothing
  }

  it should "return the input document for an empty path" in {
    val json           = parse(inputMessageJson)
    val result: JValue = getKey("", json)

    result shouldBe json
    //a[RuntimeException] shouldBe thrownBy(getKey("", json))
  }

  it should "flatten a json document" in {
    val json      = parse(inputMessageJson)
    val flattened = parse(flattenedInputMessageJson)
    val result    = flatten(json)

    //info(pretty(render(result)))

    result shouldBe flattened
  }

  it should "flatten a json document containing an array" in {
    import JsonMethods._ // pretty, render, etc.
    val json      = parse(arrayDocumentJson)
    val flattened = parse(flattenedArrayDocumentJson)
    val result    = flatten(json)
    info(pretty(render(result)))

    result shouldBe flattened
  }

  it should "should fail when trying to retrieve invalid xpath key TODO" in {
    val json = parse(inputMessageJson)

    a[RuntimeException] shouldBe thrownBy(getKey("invalid$path", json))
  }

}

object Json4sResearchSpec {

  val arrayDocumentJson =
    """
      |{
      |  "myArray": [
      |    1,
      |    2,
      |    3,
      |    5,
      |    7,
      |    11
      |  ]
      |}
      |""".stripMargin

  val flattenedArrayDocumentJson =
    """
      |{
      |  "myArray.0":1,
      |  "myArray.1":2,
      |  "myArray.2":3,
      |  "myArray.3":5,
      |  "myArray.4":7,
      |  "myArray.5":11
      |} 
      |
      |""".stripMargin

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

  val flattenedInputMessageJson =
    """
      |{
      |  "id": 1234453722394796032,
      |  "timestamp": "2020-03-02T12:21:15.000Z",
      |  "server.timestamp": "2020-03-02T12:21:17.110Z",
      |  "attributes.manufacturer": "Calamp",
      |  "attributes.model": "Calamp32Bits",
      |  "attributes.identifier": "1232064621",
      |  "features.battery.voltage": 13600.0,
      |  "features.engine.running.status": true,
      |  "features.location.coordinate.lat": -12.21099,
      |  "features.location.coordinate.lng": -76.961865,
      |  "features.location.altitude": 8.916,
      |  "features.location.speed": 38,
      |  "features.location.course": 240,
      |  "features.vehicle.ignition.status": true
      |}
      |""".stripMargin

}
