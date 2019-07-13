package com.cmartin.learn.future

import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

class FutureSpec extends AsyncFlatSpec {

  "Future fold" should "sum the results" in {
    val f1 = Future {
      2
    }
    val f2 = Future {
      3
    }

    val seq: List[Future[Int]] = f1 :: f2 :: Nil

    val result = Future.foldLeft(seq)(0)((a, b) => a + b)


    result map { s =>
      assert(s == 5)
    }

  }

}
