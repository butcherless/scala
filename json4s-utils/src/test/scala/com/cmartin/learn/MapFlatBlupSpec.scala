package com.cmartin.learn

import com.cmartin.learn.TestSamples._
import org.json4s.native.JsonMethods
import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MapFlatBlupSpec extends AnyFlatSpec with Matchers {
  behavior of "MapFlatBlup"

  /*
    F L A T T E N
   */

  it should "flatten json keys in a Json Object" in {
    val resultMap: Map[String, Any] = MapFlatBlup.flatten(nestedJson).value

    val expectedMap: Map[String, Any] =
      JsonMethods.parse(flattenedJson).extract[Map[String, Any]]

    resultMap shouldBe expectedMap
  }

  it should "get a None value for an invalid nested json" in {
    val result: Option[Map[String, Any]] =
      MapFlatBlup.flatten(invalidNestedJson)

    result shouldBe None
  }

  ignore should "flatten json keys in a Json Array" in {
    val resultMap: Map[String, Any] = MapFlatBlup.flatten(nestedArrayJson).value

    resultMap shouldBe JsonMethods
      .parse(flattenedArrayJson)
      .extract[Map[String, Any]]
  }

  /*
     B L O W U P
   */

  ignore should "blow up json keys in a flattened Json Object" in {
    val resultMap: Map[String, Any] = MapFlatBlup.blowup(flattenedJson).value

    val expectedMap: Map[String, Any] =
      JsonMethods.parse(nestedJson).extract[Map[String, Any]]

    resultMap shouldBe expectedMap
  }

  ignore should "get a None value for an invalid flattened json" in {
    val result: Option[Map[String, Any]] =
      MapFlatBlup.blowup(invalidFlattenedJson)

    result shouldBe None
  }
}
