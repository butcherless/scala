package com.cmartin.learn.traverse

import org.scalatest.flatspec.AnyFlatSpec

class OptionTraverseSpec extends AnyFlatSpec {
  behavior of "Option Traverse function"

  it should "retrieve a list with a sequenced service responses" in {
    // simulates an arbitrary list of services
    val serviceList = List(Ok, Ok, Ko, Ko, Ok)

    val result: Seq[Option[Int]] = combineServiceResponses(serviceList)

    assert(
      result.filter(_.isEmpty).size + result
        .filter(_.isDefined)
        .size == serviceList.size
    )

    assert(result.contains(None))
  }
}
