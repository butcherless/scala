package com.cmartin

import java.time.LocalDate

import com.cmartin.PersonalCalendar._
import com.cmartin.model._
import org.scalatest._

/**
 * Created by cmartin on 02/02/2017.
 */
class DayFeatureSpec extends FeatureSpec with GivenWhenThen with Matchers {
  val weekenddate_2017_01_01 = LocalDate.ofYearDay(2017, 1)
  val workdate_2017_01_02 = LocalDate.ofYearDay(2017, 2)

  info("As a Day class user")

  feature("Day object") {

    scenario("create a work day with valid date") {
      Given("work day instance")
      val myWorkDay = WorkDay(workdate_2017_01_02)

      Then("work day predicates validation should be Ok")
      val positivePredicates = List(isWorkDay(_), isWorkableDay(_))
      positivePredicates.forall(_(myWorkDay)) should be(true)

      Then("work day predicates validation should be Ok")
      val negativePredicates =
        List(isAbsenceDay(_), isAnyHoliday(_), isCountryHoliday(_), isHoliday(_), isWeekendDay(_))
      negativePredicates.forall(_(myWorkDay)) should be(false)
    }

    scenario("create a weekend day with valid date") {
      Given("weekend day instance")
      val weekendDay = WeekendDay(weekenddate_2017_01_01)

      Then("weekend day predicates validation should be Ok")
      val positivePredicates = List(isWeekendDay(_))
      positivePredicates.forall(_(weekendDay)) should be(true)

      Then("weekend day predicates validation should be Ok")
      val negativePredicates =
        List(isAbsenceDay(_), isAnyHoliday(_), isCountryHoliday(_), isHoliday(_), isWorkDay(_), isWorkableDay(_))
      negativePredicates.forall(_(weekendDay)) should be(false)
    }

    scenario("create a holiday day with dummy date") {
      Given("holiday day instance")
      val holidayDay = HolidayDay(LocalDate.now())

      Then("holiday day predicates validation should be Ok")
      val positivePredicates = List(isAnyHoliday(_), isHoliday(_))
      positivePredicates.forall(_(holidayDay)) should be(true)

      Then("holiday day predicates validation should be Ok")
      val negativePredicates =
        List(isAbsenceDay(_), isCountryHoliday(_), isWorkDay(_), isWorkableDay(_), isWeekendDay(_))
      negativePredicates.forall(_(holidayDay)) should be(false)
    }

    scenario("create a country holiday day with dummy date") {
      Given("country holiday day instance")
      val countryHolidayDay = CountryHolidayDay(LocalDate.now())

      Then("country holiday day predicates validation should be Ok")
      val positivePredicates = List(isAnyHoliday(_), isCountryHoliday(_))
      positivePredicates.forall(_(countryHolidayDay)) should be(true)

      Then("country holiday day predicates validation should be Ok")
      val negativePredicates =
        List(isAbsenceDay(_), isHoliday(_), isWorkDay(_), isWorkableDay(_), isWeekendDay(_))
      negativePredicates.forall(_(countryHolidayDay)) should be(false)
    }

    scenario("create an absence day with dummy date") {
      Given("absence day instance")
      val absenceDay = AbsenceDay(LocalDate.now)

      Then("absence holiday day predicates validation should be Ok")
      val positivePredicates = List(isAbsenceDay(_))
      positivePredicates.forall(_(absenceDay)) should be(true)

      Then("absence holiday day predicates validation should be Ok")
      val negativePredicates =
        List(isAnyHoliday(_), isHoliday(_), isWorkDay(_), isWorkableDay(_), isWeekendDay(_))
      negativePredicates.forall(_(absenceDay)) should be(false)
    }
  }
}