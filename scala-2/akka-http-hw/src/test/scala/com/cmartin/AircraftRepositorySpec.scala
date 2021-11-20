package com.cmartin

import java.time.LocalDate

import com.cmartin.data.{Aircraft, AircraftRepository}
import org.scalatest.BeforeAndAfter
import org.scalatest.OptionValues._
import org.scalatest.TryValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AircraftRepositorySpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfter {
  private val AIRCRAFT_ID = "EC-MXV"
  private val AIR_EUROPA_AIRLINE = "Air Europa"
  private val IBERIA_AIRLINE = "Iberia"

  val repo = new AircraftRepository()

  before {
    repo.removeAll()
  }

  "The aircraft repo" should "be empty" in {
    val result = repo.isEmpty
    result.success.value shouldBe true
  }

  "The aircraft repo count" should "be zero" in {
    val result = repo.count()
    result.success.value
    0
  }

  "The getAll function" should "return an empty list" in {
    val result = repo.getAll
    result.isSuccess shouldBe true
    result.get.isEmpty shouldBe true
  }

  "The save function" should "store the entity into the repository" in {
    val result = repo.save(buildAircraft(AIRCRAFT_ID))
    result.success.value shouldBe true // Try/Boolean
    assertRepoCount(1)
  }

  "The getById function" should "return an entity" in {
    val expected = buildAircraft(AIRCRAFT_ID)
    repo.save(expected)
    val actualTry = repo.getById(AIRCRAFT_ID)
    expected shouldBe actualTry.success.value.value // Try/Option/Aircraft
    assertRepoCount(1)
  }

  "The update function" should "change the entity" in {
    val initial = buildAircraft(AIRCRAFT_ID)
    repo.save(initial)
    val initialTry = repo.getById(initial.id)
    initialTry.success.value.value shouldBe initial
    val updated = initial.copy(typeCode = "A346")
    repo.save(updated)
    val updatedTry = repo.getById(updated.id)
    updatedTry.success.value.value shouldBe updated
    assertRepoCount(1)
  }

  "The remove function" should "delete the entity from repository" in {
    val initial = buildAircraft(AIRCRAFT_ID)
    repo.save(initial)
    val initialTry = repo.getById(initial.id)
    assertRepoCount(1)
    val result = repo.remove(initialTry.success.value.value)
    result.success.value shouldBe true
    assertRepoCount(0)
  }

  "The getAll(filter) function" should "return a sublist of entities from repository" in {
    buildAircraftList()
    assertRepoCount(3)
    val subList1 = repo.getAll(_.airline == AIR_EUROPA_AIRLINE)
    subList1.success.value.size shouldBe 2

    val subList2 = repo.getAll(_.typeCode == "A359")
    subList2.success.value.size shouldBe 1

    val subList3 = repo.getAll(_.desc == "Tenerife")
    subList3.success.value.size shouldBe 0
  }

  private def assertRepoCount(c: Int) = repo.count().get shouldBe c

  private def buildAircraft(id: String, airline: String) =
    Aircraft(id, "A332", airline, "Sierra de Cazorla", LocalDate.now)

  private def buildAircraft(id: String): Aircraft =
    buildAircraft(id, IBERIA_AIRLINE)

  /*
  Creates 3 aircraft
   */
  private def buildAircraftList() = {
    val a1 = Aircraft(
      "ec-mab",
      "B787",
      AIR_EUROPA_AIRLINE,
      "Islas Canarias",
      LocalDate.now
    )
    val a2 = Aircraft(
      "ec-ncd",
      "A332",
      AIR_EUROPA_AIRLINE,
      "Islas Baleares",
      LocalDate.now
    )
    val a3 = Aircraft(
      "ec-mxv",
      "A359",
      IBERIA_AIRLINE,
      "Plácido Domingo",
      LocalDate.now
    )
    repo.save(a1)
    repo.save(a2)
    repo.save(a3)
  }
}
