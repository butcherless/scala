package com.cmartin.learn.traverse

import org.scalatest.FlatSpec

class OptionTraverseSpec extends FlatSpec {

  behavior of "Option Traverse function"

  it should "retrieve a list with a sequenced service responses" in {
    // simulates an arbitrary list of services
    val serviceList = List(Ok, Ok, Ko, Ko, Ok)

    val result: Seq[Option[Int]] = combineServiceResponses(serviceList)

    assert(
      result.filter(_.isEmpty).size + result.filter(_.isDefined).size == serviceList.size
    )

    assert(result.contains(None))
  }

}
