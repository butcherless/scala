import MyLibrary._

/**
  * Created by cmartin on 27/05/16.
  */
object MyModuleMain {

  def main(args: Array[String]): Unit = {
    println("MyModuleMain test message")
    println(formatAbs(64))
    println(formatAbs(-1024))
    println("string reverse: " + stringReverse("reverse"))
  }
}

# this leads to a compilation error