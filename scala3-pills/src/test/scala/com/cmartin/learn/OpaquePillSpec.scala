package com.cmartin.learn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import OpaquePill.CompanyIdOT._
import OpaquePill.ObjectIdOT._
import OpaquePill._

class OpaquePillSpec extends AnyFlatSpec with Matchers {

  it should "give business semantics to a simple Long type" in {
    val cid = CompanyId(7L)
    val oid = ObjectId(11L)
    val device = Device(cid, oid)

    device.companyId.toLong shouldBe 7L
    device.objectId.toLong shouldBe 11L
  }
}
