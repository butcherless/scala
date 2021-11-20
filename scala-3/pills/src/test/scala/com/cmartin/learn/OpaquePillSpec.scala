package com.cmartin.learn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import OpaquePill.CompanyIdOT._
import OpaquePill.ObjectIdOT._
import OpaquePill.DeviceNameOT._
import OpaquePill.Device

class OpaquePillSpec extends AnyFlatSpec with Matchers {

  it should "give business semantics to a simple Long type" in {
    val cid = CompanyId(7L)
    val oid = ObjectId(11L)
    val name = DeviceName("TempChecker-123")

    val device = Device(cid, oid, name)

    device.companyId.toLong shouldBe 7L
    device.objectId.toLong shouldBe 11L
    device.name.toString shouldBe "TempChecker-123"
  }
}
