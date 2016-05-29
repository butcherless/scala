

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

  def intToString(x: Int): String = {
    def extractDigits(x: Int): String = {
      val quotient = x / 10
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
    def go(s: String, n: Int): Int = {
      if (s.isEmpty) n
      else go(s.tail, n + 1)
    }

    go(s, 0)
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
}
