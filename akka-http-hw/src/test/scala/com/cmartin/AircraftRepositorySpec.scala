package com.cmartin

import com.cmartin.data.data.AircraftRepository
import org.scalatest.{FlatSpec, Matchers}

class AircraftRepositorySpec extends FlatSpec with Matchers {

  val repo = new AircraftRepository()

  "The aircraft repo" should "be empty" in {
    val result = repo.isEmpty()
    result.isSuccess shouldBe true
    result.get shouldBe true
  }

  "The aircraft repo count" should "be zero" in {
    val result = repo.count()
    result.isSuccess shouldBe true
    result.get shouldBe 0
  }

  "The aircraft getAll function" should "be return an empty list" in {
    val result = repo.getAll()
    result.isSuccess shouldBe true
    result.get.isEmpty shouldBe true
  }

}
