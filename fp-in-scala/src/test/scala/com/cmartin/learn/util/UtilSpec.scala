package com.cmartin.learn.util

import org.scalatest.{FlatSpec, Matchers}

class UtilSpec extends FlatSpec with Matchers {

  behavior of "copy map function"

  it should "copy the empty map " in {
    val map: Map[String, Any] = Map.empty[String, Any]

    val copiedMap = copy(map)

    copiedMap.isEmpty shouldBe true
    copiedMap shouldBe map
  }

  it should "copy a non recursive map" in {
    val map: Map[String, Any] = Map("k1" -> 1, "k2" -> "v2", "k3" -> 3.0)

    val copiedMap = copy(map)

    copiedMap shouldBe map
  }

  it should "copy a recursive map" in {
    val map: Map[String, Any] = Map(
      "k1" -> 1,
      "k2" -> "value 2 map 1",
      "k3" -> Map("k1" -> "value 1 map 2", "k2" -> 2),
      "k4" -> "value 4 map 1"
    )

    val copiedMap = copy(map)

    copiedMap shouldBe map
  }

  behavior of "debug map function"

  it should "debug a non recursive map" in {
    val map: Map[String, Any] = Map("k1" -> 1, "k2" -> "v2", "k3" -> 3.0)

    debug(map)
  }

  it should "debug a recursive map" in {
    val map: Map[String, Any] = Map(
      "k1" -> 1,
      "k2" -> "value 2 map 1",
      "k3" -> Map(
        "k4" -> "value 1 map 2",
        "k5" -> Map("k6" -> "value 1 map 2"),
        "k7" -> "value 4 map 1"
      )
    )

    debug(map)
  }
}
