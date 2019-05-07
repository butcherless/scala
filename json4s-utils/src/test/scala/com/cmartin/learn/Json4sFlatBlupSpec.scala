package com.cmartin.learn

import com.cmartin.learn.StringExtensions.StringExtensionsOps
import org.json4s.JsonAST.JNothing
import org.json4s.native.JsonMethods
import org.json4s.{DefaultFormats, Diff, JValue}
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class Json4sFlatBlupSpec extends FlatSpec with Matchers {

  val nestedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2": {
      |    "k21": 2.0,
      |    "k22" : 3
      |  },
      |  "k3": null,
      |  "k4": {
      |    "k41": {
      |      "k411": "value-5",
      |      "k412": true
      |    },
      |    "k42": "value-6",
      |    "k43": [1,2,3],
      |    "k44": []
      |  }
      |}
    """.stripMargin.removeSpaces

  val flattenedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2.k21": 2.0,
      |  "k2.k22": 3,
      |  "k3": null,
      |  "k4.k41.k411": "value-5",
      |  "k4.k41.k412": true,
      |  "k4.k42": "value-6",
      |  "k4.k43.[0]": 1,
      |  "k4.k43.[1]": 2,
      |  "k4.k43.[2]": 3,
      |  "k4.k44": []
      |}
    """.stripMargin.removeSpaces

  val nestedArrayJson: String =
    """
      |[
      |{
      |  "k1": "value-1",
      |  "k2": {
      |    "k21": 2.0,
      |    "k22" : 3
      |  },
      |  "k3": null
      |},
      |{
      |  "k4": {
      |    "k41": {
      |      "k411": "value-5",
      |      "k412": true
      |    }
      |  }
      |}
      |]
    """.stripMargin.removeSpaces

  val flattenedArrayJson: String =
    """
      |{
      |"[0].k1": "value-1",
      |"[0].k2.k21": 2.0,
      |"[0].k2.k22": 3,
      |"[0].k3": null,
      |"[1].k4.k41.k411": "value-5",
      |"[1].k4.k41.k412": true
      |}
    """.stripMargin.removeSpaces


  val invalidNestedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2": {
      |    "k21": 9.99,
      |    "k22" : 9
      |  },
      |  "k3": null,
      |  "k4": {
      |    "k41": {
      |      "k411": INVALID,
      |      "k412": true
      |    },
      |    "k42": "value-6"
      |  }
      |}
    """.stripMargin.removeSpaces

  val invalidFlattenedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2.k21": 2.0,
      |  "k2.k22": 3,
      |  "k3": null,
      |  "k4.k41.k411": "value-5",
      |  "k4.k41.k412": true,
      |  "k4.k42": "value-6,
      |  "k4.k43.[0]": 1,
      |  "k4.k43.[1]": 2,
      |  "k4.k43.[2]": 3
      |}
    """.stripMargin.removeSpaces


  behavior of "Json4sFlatBlup"

  implicit val formats: DefaultFormats = org.json4s.DefaultFormats


  /*
     F L A T T E N
   */

  it should "flatten json keys in a Json Object" in {

    val result: String = Json4sFlatBlup.flatten(nestedJson).value

    val resultAst: JValue = JsonMethods.parse(result)
    val expectedAst: JValue = JsonMethods.parse(flattenedJson)

    resultAst diff expectedAst shouldBe Diff(JNothing, JNothing, JNothing)

  }

  it should "flatten json keys in a Json Object returning a Map" in {

    val resultMap: Map[String, Any] = Json4sFlatBlup.flattenToMap(nestedJson).value

    //    val resultAst: JValue = JsonMethods.parse(result)
    val expectedMap: Map[String, Any] = JsonMethods.parse(flattenedJson).extract[Map[String, Any]]
    //
    resultMap shouldBe expectedMap
  }


  it should "get a None value for an invalid nested json" in {
    val result: Option[String] = Json4sFlatBlup.flatten(invalidNestedJson)

    result shouldBe None
  }

  it should "flatten json keys in a Json Array" in {
    val result: String = Json4sFlatBlup.flatten(nestedArrayJson).value

    result shouldBe flattenedArrayJson
  }


  /*
     B L O W U P
   */


  it should "blow up json keys in a flattened Json Object" in {

    val result: String = Json4sFlatBlup.blowup(flattenedJson).value

    /* el orden de los elementos del json no está garantizado
       por lo que hay que comparar via AST
     */
    val resultAst: JValue = JsonMethods.parse(result)
    val expectedAst: JValue = JsonMethods.parse(nestedJson)

    resultAst diff expectedAst shouldBe Diff(JNothing, JNothing, JNothing)
  }

  it should "get a None value for an invalid flattened json" in {
    val result: Option[String] = Json4sFlatBlup.blowup(invalidFlattenedJson)

    result shouldBe None
  }

  ignore should "blow up nested keys in a Json array" in {

    val result: String = Json4sFlatBlup.blowup(flattenedArrayJson).value

    result shouldBe nestedArrayJson
  }

}
