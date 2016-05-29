import MyLibrary.intToString

object PreownedKittenMain extends App {
  println("Hello, sbt world!")

  var integer = 1234567890
  println("result: " + intToString(integer))
  integer = -987654321
  println("result: " + intToString(integer))

}
