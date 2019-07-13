package com.cmartin.learn.future

import cats.data._
import cats.implicits._
import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future
import scala.util.Random

// https://gist.github.com/rtitle/f73d35e79a2f95871bca27d24be3a805
class FutureSpec extends AsyncFlatSpec {

  val s1Result: Future[String] = buildOkResponseFuture(1)

  val s2Result: Future[String] = buildOkResponseFuture(2)

  val s3Result: Future[String] = buildOkResponseFuture(3)

  val sE1Result: Future[String] = Future {
    println("service E1 request")
    throw new RuntimeException("service E1 failed")
  }

  val sE2Result: Future[String] = Future {
    println("service E2 request")
    throw new RuntimeException("service E2 failed")
  }

  val delayMax = 500

  def getDelay(maxDelay: Int): Int = Random.nextDouble() * maxDelay toInt

  def buildOkResponseFuture(number: Int): Future[String] = {
    Future {
      val delay = getDelay(delayMax)
      println(s"service[$number] request with delay[$delay]")
      Thread.sleep(getDelay(delay))
      s"succesful result[${number}umber]"
    }
  }

  implicit class EnrichedFuture[A](future: Future[A]) {
    def toValidatedNel: Future[ValidatedNel[Throwable, A]] = {
      future.map(Validated.valid).recover { case e =>
        Validated.invalidNel(e)
      }
    }
  }


  "Future fold" should "sum the results" in {
    val f1 = Future(1)
    val f2 = Future(2)
    val f3 = Future(3)

    val seq: List[Future[Int]] = f1 :: f2 :: f3 :: Nil

    val result = Future.foldLeft(seq)(0)((a, b) => a + b)

    result map { s =>
      assert(s == 6)
    }
  }

  "Future list execution" should "return a full valid response list" in {
    val resultList: List[Future[String]] = List(s1Result, s2Result, s3Result)

    val result: Future[List[ValidatedNel[Throwable, String]]] = resultList.traverse(res => res.toValidatedNel)

    result map { list =>
      assert(list.forall(_.isValid))
    }
  }

  it should "return a response list with valid and invalid responses" in {
    val resultList: List[Future[String]] = List(s1Result, s2Result, sE1Result, sE2Result)

    val result: Future[List[ValidatedNel[Throwable, String]]] = resultList.traverse(res => res.toValidatedNel)

    result map { list =>
      assert(list.filter(_.isValid).size == 2)
      assert(list.filter(_.isInvalid).size == 2)
    }
  }


  it should "return a full invalid response list" in {
    val resultList: List[Future[String]] = List(sE1Result, sE2Result)

    val result: Future[List[ValidatedNel[Throwable, String]]] = resultList.traverse(res => res.toValidatedNel)

    result map { list =>
      assert(list.forall(_.isInvalid))
    }
  }

}
