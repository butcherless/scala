package com.cmartin.learn

import com.cmartin.learn.TestSamples._
import org.json4s.JsonAST.JNothing
import org.json4s.native.JsonMethods
import org.json4s.{DefaultFormats, Diff, JValue}
import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Json4sFlatBlupSpec extends AnyFlatSpec with Matchers {
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
