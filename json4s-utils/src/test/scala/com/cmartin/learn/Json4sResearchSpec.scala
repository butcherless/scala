package com.cmartin.learn

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import com.cmartin.learn.Json4sResearch._
import org.json4s.JsonAST.JDouble
import org.json4s.JsonAST.JNothing
import org.json4s.JsonAST.JObject
import org.json4s.JsonAST.JValue
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Json4sResearchSpec extends AnyFlatSpec with Matchers {
  import Json4sResearchSpec._
  import Json4sResearchTestUtils._

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

  it should "get an existing key in a json document with a simple type value" in {
    val json           = inputMessageJson
    val result: JValue = getKey("features.location.coordinate.lat", json)

    result shouldBe JDouble(-12.21099)
  }

  it should "get an existing key in a json document with an object value" in {
    val json           = inputMessageJson
    val result: JValue = getKey("features.location.coordinate", json)

    result shouldBe JObject(
      ("lat", JDouble(-12.21099)) :: ("lng", JDouble(-76.961865)) :: Nil
    )
  }

  it should "return the JNothing value for a non-existing key in a json document" in {
    val json           = inputMessageJson
    val result: JValue = getKey("features.xxx.lat", json)

    result shouldBe JNothing
  }

  it should "return the input document for an empty path" in {
    val json           = inputMessageJson
    val result: JValue = getKey("", json)

    result shouldBe json
    // a[RuntimeException] shouldBe thrownBy(getKey("", json))
  }

  it should "flatten a json document" in {
    val json      = inputMessageJson
    val flattened = flattenedInputMessageJson
    val result    = flatten(json)

    // info(pretty(render(result)))

    result shouldBe flattened
  }

  it should "flatten a json document containing an array" in {
    val json      = arrayDocumentJson
    val flattened = flattenedArrayDocumentJson
    val result    = flatten(json)
    // info(jValueToString(result))

    result shouldBe flattened
  }

  it should "fail when trying to retrieve invalid xpath key" in {
    val json = inputMessageJson

    a[RuntimeException] shouldBe thrownBy(getKey("invalid$path", json))
  }

  it should "return an ISO8601 representation for a date" in {
    val expectedDateText = "2020-06-10T04:21:13Z"

    val zdt = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
    info(zdt)

    dateText shouldBe expectedDateText
  }

  it should "add new contents to the entity state" in {
    val j1 = json3String
    val j2 = json4String

    // j1 was the previous state in the repository
    val diff   = j1 diff j2
    val merged = j1 merge diff.added

    merged shouldBe j2
  }

  it should "update contents to the entity state" in {
    val j1 = json3String
    // j2 is the new state, change contents
    val j2 = json5String

    // j1 was the previous state in the repository
    val diff   = j1 diff j2
    val merged = j1 merge diff.changed

    merged shouldBe j2
  }

  it should "add and update contents to the entity state" in {
    val current  = json1String
    val expected = json1_6String // added and changed
    // the incoming state, add and change contents
    val incoming = json6String

    // j1 was the previous state in the repository
    val merged = mergeShadows(current, incoming)

    merged shouldBe expected
  }

  it should "exclude a key from the document, single value" in {
    val json           = jsonForExcluding
    val result: JValue =
      json
        .replace(splitPath("k1"), JNothing)
        .remove(_ == JNothing) // removed when rendering

    result shouldBe excludedK1Json
  }

  it should "exclude a nested key from the document, single value TODO" in {
    val json           = jsonForExcluding
    val result: JValue =
      json
        .replace(splitPath("k2.k21"), JNothing)
        .remove(_ == JNothing) // removed when rendering

    result shouldBe excludedK21Json
  }

  it should "exclude a nested key from the document, object value TODO" in {
    val json           = jsonForExcluding
    val result: JValue =
      json
        .replace(splitPath("k2.k23"), JNothing)
        .remove(_ == JNothing) // removed when rendering

    result shouldBe excludedK23Json
  }

  it should "exclude a list of keys from the document TODO" in {
    val keys           = List("k1", "k2.k21", "k2.k23")
    val json           = jsonForExcluding
    val result: JValue =
      excludeKeys(keys, json)
        .remove(_ == JNothing) // removed when rendering

    result shouldBe excludedKyeListJson
  }

}

object Json4sResearchSpec {

  val arrayDocumentJson: JValue =
    parse("""
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
      |""".stripMargin)

  val flattenedArrayDocumentJson: JValue =
    parse("""
      |{
      |  "myArray.0":1,
      |  "myArray.1":2,
      |  "myArray.2":3,
      |  "myArray.3":5,
      |  "myArray.4":7,
      |  "myArray.5":11
      |} 
      |
      |""".stripMargin)

  val inputMessageJson: JValue =
    parse("""
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
      |""".stripMargin)

  val flattenedInputMessageJson: JValue =
    parse("""
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
      |""".stripMargin)

  val json1String: JValue =
    parse("""
      |{
      |  "id": 1234,
      |  "value1": "alfa",
      |  "value3": "charlie"
      |}
      |""".stripMargin)

  val json2String: String =
    """
      |{
      |  "id": 1234,
      |  "value1": "alfa",
      |  "value2": "bravo"
      |}
      |""".stripMargin

  val json3String: JValue =
    parse("""
    |{
    |  "id": 1234
    |}
    |""".stripMargin)

  val json4String: JValue =
    parse("""
      |{
      |  "id": 1234,
      |  "value1": "alfa"
      |}
      |""".stripMargin)

  val json5String: JValue =
    parse("""
      |{
      |  "id": 5678
      |}
      |""".stripMargin)

  val json6String: JValue =
    parse("""
      |{
      |  "id": 1234,
      |  "value1": "tango",
      |  "value4": "x-ray"
      |}
      |""".stripMargin)

  val json1_6String: JValue =
    parse("""
      |{
      |  "id": 1234,
      |  "value1": "tango",
      |  "value4": "x-ray",
      |  "value3": "charlie"
      |}
      |""".stripMargin)

  val jsonForExcluding: JValue =
    parse("""
        |{
        |  "k1": 1,
        |  "k2": {
        |    "k21": 21,
        |    "k22": 22,
        |    "k23": {
        |      "k231": 231,
        |      "k232": 232
        |    }
        |  },
        |  "k3": 3
        |}
        |""".stripMargin)

  val excludedK1Json: JValue =
    parse("""
        |{
        |  "k2": {
        |    "k21": 21,
        |    "k22": 22,
        |    "k23": {
        |      "k231": 231,
        |      "k232": 232
        |    }
        |  },
        |  "k3": 3
        |}
        |""".stripMargin)

  val excludedK21Json: JValue =
    parse("""
        |{
        |  "k1": 1,
        |  "k2": {
        |    "k22": 22,
        |    "k23": {
        |      "k231": 231,
        |      "k232": 232
        |    }
        |  },
        |  "k3": 3
        |}
        |""".stripMargin)

  val excludedK23Json: JValue =
    parse("""
        |{
        |  "k1": 1,
        |  "k2": {
        |    "k21": 21,
        |    "k22": 22
        |  },
        |  "k3": 3
        |}
        |""".stripMargin)

  val excludedKyeListJson: JValue =
    parse("""
        |{
        |  "k2": {
        |    "k22": 22
        |  },
        |  "k3": 3
        |}
        |""".stripMargin)

}
