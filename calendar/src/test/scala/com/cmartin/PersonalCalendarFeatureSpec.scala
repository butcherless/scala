package com.cmartin

import com.cmartin.PersonalCalendar.{isAbsenceDay, isCountryHoliday, isHoliday, isWorkDay, isWorkableDay, isWeekendDay}
import com.cmartin.model.{WorkDay}
import org.scalatest._

/**
  * Created by cmartin on 02/02/2017.
  */
class PersonalCalendarFeatureSpec extends FeatureSpec with GivenWhenThen with Matchers {
  info("As a PersonalCalendar class user")

  val YEAR = 2017

  feature("Constructor validation") {

    scenario("valid year value") {
      Given("personal calendar instance")
      val year = new LocalDateCalendar(YEAR)

      Then("calendar year count should be greater or equals to 365")
      val dayCount = year.dayCount
      dayCount should (be(365) or be(366))
    }

    scenario("invalid year value") {
      Given("personal calendar instance")
      an[IllegalArgumentException] should be thrownBy new LocalDateCalendar(-1)
    }
  }


  scenario("retrieving day collections") {
    Given("a year calendar")
    val year = new LocalDateCalendar(YEAR)

    Then("should exists a not empty weekend day list")
    val weekendDays = year.getWeekendDays
    weekendDays should not be (empty)
    Then("every day should be a weekend day")
    weekendDays.forall(isWeekendDay(_)) should be(true)

    Then("every day should be an absence day")
    year.getAbsenceDays.forall(isAbsenceDay(_)) should be(true)

    Then("every day should be a holidays day")
    year.getHolidayDays.forall(isHoliday(_)) should be(true)

    Then("every day should be a holidays day")
    year.getCountryHolidayDays.forall(isCountryHoliday(_)) should be(true)

    Then("should exists a work day list")
    val workDays = year.getWorkDays
    Then("every day should be a work day")
    workDays.forall(isWorkDay(_)) should be(true)

    Then("should exists a work day list")
    val workableDays = year.getWorkableDay
    Then("every day should be a work day")
    workableDays.forall(isWorkableDay(_)) should be(true)

    Then("number of workable days should be greater or equals than number of work days due to number of absences")
    workableDays.size should be >= workDays.size
  }
}
