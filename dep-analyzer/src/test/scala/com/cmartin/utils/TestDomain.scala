package com.cmartin.utils

object TestDomain {
  sealed trait MyTestException extends Exception

  case class MyException1(m: String) extends MyTestException

  case class MyException2(m: String) extends MyTestException

}
