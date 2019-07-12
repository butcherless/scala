package com.cmartin.learn.traverse

import org.scalatest.AsyncFlatSpec
import org.scalatest.concurrent.TimeLimits

import scala.concurrent.Future
import scala.concurrent.duration._

class FutureTraverseSpec extends AsyncFlatSpec with TimeLimits {

  behavior of "Future Traverse function"

  it should "retrieve a list with a sequenced service responses" in {
    // simulates an arbitrary list of services
    val serviceList = List(Ko, Ok, Ok, Ko, Ok)
    failAfter(2 * delayMax milliseconds) {

      val result: Future[Seq[ServiceResponse]] = composeServiceResponses(serviceList)

      result map { responses =>
        assert(responses.size == serviceList.size)
        assert(responses.filter(_.isRight).size == 3)
        assert(responses.filter(_.isLeft).size == 2)
      }
    }
  }

}
