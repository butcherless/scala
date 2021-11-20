import MyLibrary._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Created by cmartin on 16/05/16.
  */
class MyLibrarySpec extends AnyFlatSpec with Matchers {

  val EMPTY_STRING = ""
  val STRING_1 = "A"
  val STRING_10 = "1234567890"
  val STRING_SAUDADE = "saudade"

  it should "intToString '0' specification" in {
    val result = intToString(0)
    result should have length 1
    result.toInt shouldBe 0
  }

  it should "intToString '1024' size specification" in {
    val result = intToString(1024)
    result should have length 4
    result.toInt shouldBe 1024
  }

  it should "intToString '-1024' size specification" in {
    val result = intToString(-1024)
    result should have length 5
    result.toInt shouldBe (-1024)
    result should startWith("-")
  }

  it should "stringLength '0' emptyString specification" in {
    val result = stringLength(EMPTY_STRING)
    result shouldBe 0
    EMPTY_STRING should have length 0
  }

  it should "stringLength '1' String size specification" in {
    val result = stringLength(STRING_1)
    result shouldBe 1
    STRING_1 should have length 1
  }

  it should "stringLength '10' String size specification" in {
    val result = stringLength(STRING_10)
    result shouldBe 10
    STRING_10 should have length 10
  }

  it should "stringReverse 'saudade' String  size specification" in {
    val result = stringReverse(STRING_SAUDADE)
    result shouldBe STRING_SAUDADE.reverse
  }

  it should "stringReverse emptyString specification" in {
    val result = stringReverse("")
    result shouldBe ""
  }

  it should "intSum '1..10' specification" in {
    val result = intSum((1 to 10).toList)
    result shouldBe 55
  }

  it should "intSum '0' specification" in {
    val result = intSum(List(0))
    result shouldBe 0
  }

  it should "pow10 '0' specification" in {
    val result = pow10(0)
    result shouldBe 1
  }

  it should "pow10 '1' specification" in {
    val result = pow10(1)
    result shouldBe 10
  }

  it should "pow10 '6' specification" in {
    val result = pow10(6)
    result shouldBe 1000000
  }

  it should "stringToInt '' specification" in {
    val result = stringToInt("")
    result shouldBe 0
  }

  it should "stringToInt '0' specification" in {
    val result = stringToInt("0")
    result shouldBe 0
  }

  it should "stringToInt '2048' specification" in {
    val result = stringToInt("2048")
    result shouldBe 2048
  }

  val lowerString = "qwerty"
  val upperString = "QWERTY"

  it should s"""toUpper '$lowerString' specification""" in {
    val result = toUpper("qwerty")
    result shouldBe upperString
  }

  it should "sum result for Nil List is zero" in {
    val l = Nil
    val result = sum(l)
    result shouldBe 0
  }

  it should "sum result for 1..5 List is 15" in {
    val l = List(1, 2, 3, 4, 5)
    val result = sum(l)
    result shouldBe 15
  }

  it should "sumHOF result for Nil List is zero" in {
    val l = Nil
    val result = sumHOF(l)
    result shouldBe 0
  }

  it should "sumHOF result for 1..5 List is 15" in {
    val l = List(1, 2, 3, 4, 5)
    val result = sumHOF(l)
    result shouldBe 15
  }

  it should "prodHOF result for 1..5 List is 120" in {
    val l = List(1, 2, 3, 4, 5)
    val result = prodHOF(l)
    result shouldBe 120
  }

  it should "prodHOF result for 0..5 List is 0" in {
    val l = List(0, 1, 2, 3, 4, 5)
    val result = prodHOF(l)
    result shouldBe 0
  }

}
