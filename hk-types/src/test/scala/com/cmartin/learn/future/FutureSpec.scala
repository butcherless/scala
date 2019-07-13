package com.cmartin.learn.future

import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

// https://gist.github.com/rtitle/f73d35e79a2f95871bca27d24be3a805
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
