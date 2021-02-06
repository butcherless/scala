package com.cmartin.learn
import org.scalatest.Assertions.fail
import zio.prelude.fx.Cause

object Commons {
  def causeToErrorList[E, A](either: Either[Cause[E], A]) = {
    either match {
      case Left(cause)  => Left(cause.toChunk.toList)
      case Right(value) => fail(s"result should be Left: $value")
    }
  }

}
