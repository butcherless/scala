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

}
