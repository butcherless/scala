package com.cmartin.learn

import java.time.{ZoneId, ZonedDateTime}

import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods

object Json4sResearchTestUtils {
  import JsonMethods._ // pretty, render, etc.

  val payloadKey = "payload"

  val inputMessage_UC_1_1 =
    """
      |{
      |  "payload": {
      |    "providerId": 879970290359074800,
      |    "deviceIdentifier": "[R]357666050866893"
      |  }
      |}
      |""".stripMargin

  val shadowMessage_UC_1_1 =
    """
      |{
      |  "state": {
      |    "providerId": 879970290359074800,
      |    "deviceIdentifier": "[R]357666050866893"
      |  },
      |  "metadata": {
      |    "providerId": {
      |      "timestamp": "2020-06-10T04:21:13Z"
      |    },
      |    "deviceIdentifier": {
      |      "timestamp": "2020-06-10T04:21:13Z"
      |    }
      |  }
      |}
      |""".stripMargin

  val inputMessage_UC_1_2 =
    """
      |{
      |  "payload": {
      |    "providerId": 879970290359074800
      |  }
      |}
      |""".stripMargin

  val outputMessage_UC_1_2 =
    """
      |{
      |  "state": {
      |    "providerId": 879970290359074800,
      |    "deviceIdentifier": "[R]357666050866893"
      |  },
      |  "metadata": {
      |    "providerId": {
      |      "timestamp": "2020-06-10T04:22:15Z"
      |    },
      |    "deviceIdentifier": {
      |      "timestamp": "2020-06-10T04:21:13Z"
      |    }
      |  }
      |}
      |""".stripMargin

  val dateText =
    ZonedDateTime
      .of(2020, 6, 10, 4, 21, 13, 0, ZoneId.of("UTC"))
      .format(Json4sResearch.dateTimeFormater)

  val dateText2 =
    ZonedDateTime
      .of(2020, 6, 10, 4, 22, 15, 0, ZoneId.of("UTC"))
      .format(Json4sResearch.dateTimeFormater)

  def jValueToCompactString(json: JValue): String =
    compact(render(json))

  def jValueToString(json: JValue): String =
    pretty(render(json))

}
