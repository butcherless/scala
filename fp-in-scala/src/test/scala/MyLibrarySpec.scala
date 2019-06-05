import MyLibrary._

/**
 * Created by cmartin on 16/05/16.
 */
class MyLibrarySpec extends org.specs2.mutable.Specification {

  val EMPTY_STRING = ""
  val STRING_1 = "A"
  val STRING_10 = "1234567890"
  val STRING_SAUDADE = "saudade"

  "intToString '0' specification" >> {
    val result = intToString(0)
    result must have length 1
    result.toInt must be equalTo (0)
  }

  "intToString '1024' size specification" >> {
    val result = intToString(1024)
    result must have length 4
    result.toInt must be equalTo (1024)
  }

  "intToString '-1024' size specification" >> {
    val result = intToString(-1024)
    result must have length 5
    result.toInt must be equalTo (-1024)
    result must startWith("-")
  }

  "stringLength '0' emptyString specification" >> {
    val result = stringLength(EMPTY_STRING)
    result must be equalTo 0
    EMPTY_STRING must have length 0
  }

  "stringLength '1' String size specification" >> {
    val result = stringLength(STRING_1)
    result must be equalTo 1
    STRING_1 must have length 1
  }

  "stringLength '10' String size specification" >> {
    val result = stringLength(STRING_10)
    result must be equalTo 10
    STRING_10 must have length 10
  }

  "stringReverse 'saudade' String  size specification" >> {
    val result = stringReverse(STRING_SAUDADE)
    result must be equalTo (STRING_SAUDADE.reverse)
  }

  "stringReverse emptyString specification" >> {
    val result = stringReverse("")
    result must be equalTo ("")
  }

  "intSum '1..10' specification" >> {
    val result = intSum((1 to 10).toList)
    result must be equalTo (55)
  }

  "intSum '0' specification" >> {
    val result = intSum(List(0))
    result must be equalTo (0)
  }

  "pow10 '0' specification" >> {
    val result = pow10(0)
    result must be equalTo (1)
  }

  "pow10 '1' specification" >> {
    val result = pow10(1)
    result must be equalTo (10)
  }

  "pow10 '6' specification" >> {
    val result = pow10(6)
    result must be equalTo (1000000)
  }

  "stringToInt '' specification" >> {
    val result = stringToInt("")
    result must be equalTo (0)
  }

  "stringToInt '0' specification" >> {
    val result = stringToInt("0")
    result must be equalTo (0)
  }

  "stringToInt '2048' specification" >> {
    val result = stringToInt("2048")
    result must be equalTo (2048)
  }

  val lowerString = "qwerty"
  val upperString = "QWERTY"
  s"""toUpper '$lowerString' specification""" >> {
    val result = toUpper("qwerty")
    result must be equalTo (upperString)
  }

  "sum result for Nil List is zero" >> {
    val l = Nil
    val result = sum(l)
    result must be equalTo (0)
  }

  "sum result for 1..5 List is 15" >> {
    val l = List(1, 2, 3, 4, 5)
    val result = sum(l)
    result must be equalTo (15)
  }

  "sumHOF result for Nil List is zero" >> {
    val l = Nil
    val result = sumHOF(l)
    result must be equalTo (0)
  }

  "sumHOF result for 1..5 List is 15" >> {
    val l = List(1, 2, 3, 4, 5)
    val result = sumHOF(l)
    result must be equalTo (15)
  }

  "prodHOF result for 1..5 List is 120" >> {
    val l = List(1, 2, 3, 4, 5)
    val result = prodHOF(l)
    result must be equalTo (120)
  }

  "prodHOF result for 0..5 List is 0" >> {
    val l = List(0, 1, 2, 3, 4, 5)
    val result = prodHOF(l)
    result must be equalTo (0)
  }

}
