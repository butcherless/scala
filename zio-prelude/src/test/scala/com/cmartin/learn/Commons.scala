package com.cmartin.learn
import org.scalatest.Assertions.fail
import zio.Cause

object Commons {
  def causeToErrorList[E, A](either: Either[Cause[E], A]) = {
    either match {
      case Left(cause)  => Left(cause.failures)
      case Right(value) => fail(s"result should be Left: $value")
    }
  }

}
