package com.cmartin.learn.future

import cats.data._
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future
import scala.util.Random

// https://gist.github.com/rtitle/f73d35e79a2f95871bca27d24be3a805
class FutureSpec extends AsyncFlatSpec {
  import FutureSpec._

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
    val resultList: List[Future[String]] =
      List(buildOkResponseFuture(1), buildOkResponseFuture(2), buildOkResponseFuture(3))

    val result: Future[List[ValidatedNel[Throwable, String]]] = resultList.traverse(res => res.toValidatedNel)

    result map { list =>
      assert(list.forall(_.isValid))
    }
  }

  it should "return a response list with valid and invalid responses" in {
    val resultList: List[Future[String]] =
      List(buildOkResponseFuture(1), buildOkResponseFuture(2), buildKoResponseFuture(101), buildKoResponseFuture(102))

    val result: Future[List[ValidatedNel[Throwable, String]]] = resultList.traverse(res => res.toValidatedNel)

    result map { list =>
      assert(list.count(_.isValid) == 2)
      assert(list.count(_.isInvalid) == 2)
    }
  }

  it should "return a full invalid response list" in {
    val resultList: List[Future[String]] = List(
      buildKoResponseFuture(101),
      buildKoResponseFuture(102)
    )

    val result: Future[List[ValidatedNel[Throwable, String]]] = resultList.traverse(res => res.toValidatedNel)

    result map { list =>
      assert(list.forall(_.isInvalid))
    }
  }

  "Future Use Case simulator" should "run 3 parallel tasks + 1 task depending on the 3 previous ones" in {
    case class Results(repo: String, flatten: String, shadow: String)

    def f3(): Future[Results] = {
      val f1 = buildOkResponseFuture(1)
      val f2 = buildOkResponseFuture(2)
      val f3 = buildOkResponseFuture(3)

      val results = for {
        repo    <- f1
        flatten <- f2
        shadow  <- f3
      } yield Results(repo, flatten, shadow)

      results
    }

    val result = for {
      r3 <- f3() // parallel tasks
      r4 <- Future(r3.repo + r3.flatten + r3.shadow) // task waiting for 3 previous tasks
    } yield r4

    val expectedText = "Service result S[X] successful"
    result map { text =>
      assert(text.contains("Service result S[1] successful"))
      assert(text.contains("Service result S[2] successful"))
      assert(text.contains("Service result S[3] successful"))
      assert(text.length == expectedText.length * 3)
    }
  }

  it should "recover from a failing task" in {
    case class Results(repo: String, flatten: String, shadow: String)

    def f3(): Future[Results] = {
      val f1 = buildOkResponseFuture(1)
      val f2 = buildKoResponseFuture(102) // KO Task
      val f3 = buildOkResponseFuture(3)

      val results = for {
        repo    <- f1
        flatten <- f2
        shadow  <- f3
      } yield Results(repo, flatten, shadow)

      results
    }

    val result4 = for {
      r3 <- f3() // parallel tasks
      r4 <- Future(r3.repo + r3.flatten + r3.shadow) // task waiting for 3 previous tasks
    } yield r4

    val result = result4.recoverWith {
      case e: RuntimeException => Future(e.getMessage)
    }

    val expectedText = "Service result F[102] failed"
    result map { text =>
      assert(text.contains(expectedText))
      assert(text.length == expectedText.length)
    }
  }
}

object FutureSpec {
  implicit val delayMax: Int = 500

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit class EnrichedFuture[A](future: Future[A]) {
    def toValidatedNel: Future[ValidatedNel[Throwable, A]] = {
      future.map(Validated.valid).recover {
        case e =>
          Validated.invalidNel(e)
      }
    }
  }

  /*
    returns a delay between 5 and maxDelay
   */
  def getDelay(maxDelay: Int): Int = {
    val minDelay = 5
    if (maxDelay < minDelay) minDelay
    else {
      val randomDelay = Random.nextInt(maxDelay)
      if (randomDelay < 5) minDelay
      else randomDelay
    }
  }

  def buildOkResponseFuture(number: Int)(implicit maxDelay: Int): Future[String] = {
    Future {
      val delay = getDelay(maxDelay)
      println(s"Service S[$number] request with delay[$delay]")
      Thread.sleep(getDelay(delay))
      s"Service result S[${number}] successful"
    }
  }

  def buildKoResponseFuture(number: Int)(implicit maxDelay: Int): Future[String] = {
    Future {
      val delay = getDelay(maxDelay)
      println(s"Service F[$number] request with delay[$delay]")
      Thread.sleep(getDelay(delay))
      throw new RuntimeException(s"Service result F[$number] failed")
    }
  }
}
