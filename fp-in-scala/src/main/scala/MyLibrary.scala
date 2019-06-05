/**
  * Created by cmartin on 14/05/16.
  */
object MyLibrary {

  def digitToString(d: Int): String = d match {
    case 0 => "0"
    case 1 => "1"
    case 2 => "2"
    case 3 => "3"
    case 4 => "4"
    case 5 => "5"
    case 6 => "6"
    case 7 => "7"
    case 8 => "8"
    case 9 => "9"
    case _ => "?"
  }

  def stringToInt(s: String): Int = {
    def go(s: String): (Int, Int) = {
      if (s.isEmpty)
        0 -> 0
      else {
        val result = go(s.tail)
        val sum    = charToDigit(s.head) * pow10(result._2) + result._1
        val power  = result._2 + 1

        sum -> power
      }
    }

    go(s)._1
  }

  def charToDigit(c: Char): Int = {
    c - '0'.toInt
  }

  def pow10(n: Int): Int = {
    if (n == 0)
      1
    else {
      10 * pow10(n - 1)
    }
  }

  def intToString(x: Int): String = {
    def extractDigits(x: Int): String = {
      val quotient     = x / 10
      val moduleString = digitToString(x % 10)
      if (quotient == 0)
        moduleString
      else
        extractDigits(quotient) + moduleString
    }

    val signString = if (x < 0) "-" else ""
    signString + extractDigits(abs(x))
  }

  def abs(n: Int): Int = {
    if (n < 0) -n
    else n
  }

  def formatAbs(n: Int): String = {
    val message = "The absolute value of %d is %d"
    message.format(n, abs(n))
  }

  def stringLength(s: String): Int = {
    if (s.isEmpty) 0
    else stringLength(s.tail) + 1
  }

  def stringReverse(s: String): String = {
    if (s.isEmpty) {
      ""
    } else {
      stringReverse(s.tail) + s.head
    }
  }

  def intSum(l: List[Int]): Long = {
    if (l.isEmpty) 0
    else {
      l.head + intSum(l.tail)
    }
  }

  def toUpper(s: String): String = {
    if (s.isEmpty) ""
    else s.head.toUpper + toUpper(s.tail)
  }

  def sum(l: List[Int]): Int =
    l match {
      case Nil    => 0
      case h :: s => h + sum(s)
    }

  def collapse[A](l: List[A])(zero: A, add: (A, A) => A): A =
    l match {
      case Nil    => zero
      case x :: r => add(x, collapse(r)(zero, add))
    }

  def sumHOF(l: List[Int]): Int = {
    collapse(l)(0, (i1, i2) => i1 + i2)
  }

  def prodHOF(l: List[Int]): Int = {
    collapse(l)(1, (a, b) => a * b)
  }
}
