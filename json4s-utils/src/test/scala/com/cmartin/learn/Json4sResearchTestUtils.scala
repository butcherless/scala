package com.cmartin.learn

import java.time.{ZoneId, ZonedDateTime}

import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods

object Json4sResearchTestUtils {
  import JsonMethods._ // pretty, render, etc.

  val inputMessage_UC_1_1: JValue =
    parse("""
      |{
      |  "payload": {
      |    "providerId": 879970290359074800,
      |    "deviceIdentifier": "[R]357666050866893"
      |  }
      |}
      |""".stripMargin)

  val shadowMessage_UC_1_1: JValue =
    parse("""
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
      |""".stripMargin)

  val inputMessage_UC_1_2: JValue =
    parse("""
      |{
      |  "payload": {
      |    "providerId": 879970290359074800
      |  }
      |}
      |""".stripMargin)

  val shadowMessage_UC_1_2: JValue =
    parse("""
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
      |""".stripMargin)

  val inputMessage_UC_2_1: JValue =
    parse("""
      |{
      |  "payload": {
      |    "@timestamp": "2020-06-10T04:21:13Z",
      |    "providerId": 879970290359074800,
      |    "deviceIdentifier": "[R]357666050866893"
      |  }
      |}
      |""".stripMargin)

  val shadowMessage_UC_2_1: JValue =
    parse("""
      |{
      |  "state": {
      |    "@timestamp": "2020-06-10T04:21:13Z",
      |    "providerId": 879970290359074800,
      |    "deviceIdentifier": "[R]357666050866893"
      |  },
      |  "metadata": {
      |    "@timestamp": {
      |      "timestamp": "2020-06-10T04:21:13Z"
      |    },
      |    "providerId": {
      |      "timestamp": "2020-06-10T04:21:13Z"
      |    },
      |    "deviceIdentifier": {
      |      "timestamp": "2020-06-10T04:21:13Z"
      |    }
      |  }
      |}
      |""".stripMargin)

  val inputMessage_UC_2_2: JValue =
    parse("""
        |{
        |  "payload": {
        |    "@timestamp": "2020-06-10T03:30:10Z",
        |    "providerId": 1
        |  }
        |}
        |""".stripMargin)

  val shadowMessage_UC_2_2: JValue =
    parse("""
        |{
        |  "state": {
        |    "@timestamp": "2020-06-10T04:21:13Z",
        |    "providerId": 879970290359074800,
        |    "deviceIdentifier": "[R]357666050866893"
        |  },
        |  "metadata": {
        |    "@timestamp": {
        |      "timestamp": "2020-06-10T04:21:13Z"
        |    },
        |    "providerId": {
        |      "timestamp": "2020-06-10T04:21:13Z"
        |    },
        |    "deviceIdentifier": {
        |      "timestamp": "2020-06-10T04:21:13Z"
        |    }
        |  }
        |}
        |""".stripMargin)

  val inputMessage_UC_2_3: JValue =
    parse("""
            |{
            |  "payload": {
            |    "@timestamp": "2020-06-11T03:30:10Z",
            |    "providerId": 1
            |  }
            |}
            |""".stripMargin)

  val shadowMessage_UC_2_3: JValue =
    parse("""
            |{
            |  "state": {
            |    "@timestamp": "2020-06-11T03:30:10Z",
            |    "providerId": 1,
            |    "deviceIdentifier": "[R]357666050866893"
            |  },
            |  "metadata": {
            |    "@timestamp": {
            |      "timestamp": "2020-06-11T03:30:10Z"
            |    },
            |    "providerId": {
            |      "timestamp": "2020-06-11T03:30:10Z"
            |    },
            |    "deviceIdentifier": {
            |      "timestamp": "2020-06-10T04:21:13Z"
            |    }
            |  }
            |}
            |""".stripMargin)

  val inputMessage_UC_3_1: JValue =
    parse("""
            |{
            |  "metadata": {
            |    "t_field": "cts_ts"
            |  },
            |  "payload": {
            |    "cts_ts": "2020-06-10T04:21:13Z",
            |    "providerId": 879970290359074800,
            |    "deviceIdentifier": "[R]357666050866893"
            |  }
            |}
            |""".stripMargin)

  val shadowMessage_UC_3_1: JValue =
    parse("""
            |{
            |  "state": {
            |    "cts_ts": "2020-06-10T04:21:13Z",
            |    "providerId": 879970290359074800,
            |    "deviceIdentifier": "[R]357666050866893"
            |  },
            |  "metadata": {
            |    "cts_ts": {
            |      "timestamp": "2020-06-10T04:21:13Z"
            |    },
            |    "providerId": {
            |      "timestamp": "2020-06-10T04:21:13Z"
            |    },
            |    "deviceIdentifier": {
            |      "timestamp": "2020-06-10T04:21:13Z"
            |    }
            |  }
            |}
            |""".stripMargin)

  val inputMessage_UC_3_2: JValue =
    parse("""
            |{
            |  "payload": {
            |    "@timestamp": "2020-06-12T03:30:10Z",
            |    "providerId": 1
            |  }
            |}
            |""".stripMargin)

  val shadowMessage_UC_3_2: JValue =
    parse("""
            |{
            |  "state": {
            |    "cts_ts": "2020-06-10T04:21:13Z",
            |    "@timestamp": "2020-06-12T03:30:10Z",
            |    "providerId": 1,
            |    "deviceIdentifier": "[R]357666050866893"
            |  },
            |  "metadata": {
            |    "cts_ts": {
            |      "timestamp": "2020-06-10T04:21:13Z"
            |    },
            |    "@timestamp": {
            |      "timestamp": "2020-06-12T03:30:10Z"
            |    },
            |    "providerId": {
            |      "timestamp": "2020-06-12T03:30:10Z"
            |    },
            |    "deviceIdentifier": {
            |      "timestamp": "2020-06-10T04:21:13Z"
            |    }
            |  }
            |}
            |""".stripMargin)


  val dateText: String =
    ZonedDateTime
      .of(2020, 6, 10, 4, 21, 13, 0, ZoneId.of("UTC"))
      .format(Json4sResearch.dateTimeFormater)

  val dateText2: String =
    ZonedDateTime
      .of(2020, 6, 10, 4, 22, 15, 0, ZoneId.of("UTC"))
      .format(Json4sResearch.dateTimeFormater)

  def jValueToCompactString(json: JValue): String =
    compact(render(json))

  def jValueToString(json: JValue): String =
    pretty(render(json))

}
