package com.cmartin

import java.time.{DayOfWeek, LocalDate, Year}

import com.cmartin.model._

/** Created by cmartin on 21/01/2017. read file:
  * http://alvinalexander.com/scala/how-to-open-read-text-files-in-scala-cookbook-examples
  */
trait PersonalCalendar {

  def dayCount: Int

  def getAllDays: List[(Int, String)]

}

object PersonalCalendar {
  def isCountryHoliday(myDay: MyDay) = {
    myDay match {
      case CountryHolidayDay(_) => true
      case _                    => false
    }
  }

  def isWeekendDay(myDay: MyDay) = {
    myDay match {
      case WeekendDay(_) => true
      case _             => false
    }
  }

  def isHoliday(myDay: MyDay) = {
    myDay match {
      case HolidayDay(_) => true
      case _             => false
    }
  }

  def isWorkDay(myDay: MyDay) = {
    myDay match {
      case WorkDay(_) => true
      case _          => false
    }
  }

  def isAbsenceDay(myDay: MyDay) = {
    myDay match {
      case AbsenceDay(_) => true
      case _             => false
    }
  }

  def isAnyHoliday(myDay: MyDay): Boolean = {
    isCountryHoliday(myDay) || isHoliday(myDay)
  }

  def isWorkableDay(myDay: MyDay): Boolean = {
    !(isWeekendDay(myDay) || isAnyHoliday(myDay))
  }

  def isWeekendDate(d: LocalDate) = {
    d.getDayOfWeek match {
      case DayOfWeek.SATURDAY | DayOfWeek.SUNDAY => true
      case _                                     => false
    }
  }

}

class LocalDateCalendar(_year: Int) extends PersonalCalendar {
  def getAbsenceDays = yearDays.filter(PersonalCalendar.isAbsenceDay(_))

  def getCountryHolidayDays =
    yearDays.filter(PersonalCalendar.isCountryHoliday(_))

  def getHolidayDays = yearDays.filter(PersonalCalendar.isHoliday(_))

  def getWeekendDays = yearDays.filter(PersonalCalendar.isWeekendDay(_))

  def getWorkDays = yearDays.filter(PersonalCalendar.isWorkDay(_))

  def getWorkableDay = yearDays.filter(PersonalCalendar.isWorkableDay(_))

  require(_year >= 0, "the number must be non-negative")

  private val year = Year.of(_year)

  private val yearDays = buildYearDays

  override def dayCount: Int = year.length

  def readHolidays(): List[LocalDate] = {
    List(LocalDate.ofYearDay(2017, 10))
  }

  def readCountryHolidays(): List[LocalDate] = {
    List(LocalDate.ofYearDay(2017, 32))
  }

  def readAbsences(): List[LocalDate] = {
    List(LocalDate.ofYearDay(2017, 60))
  }

  private def buildYearDays: IndexedSeq[MyDay] = {
    val countryHolidays = readCountryHolidays()
    val holidays = readHolidays()
    val absences = readAbsences()

    for (
      i <- 1 to dayCount;
      date = LocalDate.ofYearDay(2017, i);
      day = getWeekendDay(date)
        .orElse(getCountryHolidaysDay(date, countryHolidays))
        .orElse(getHolidaysDay(date, holidays))
        .orElse(getAbsenceDay(date, absences))
        .orElse(getWorkDay(date))
    ) yield day.get
  }

  def getYearDays = {
    yearDays
  }

  override def getAllDays: List[(Int, String)] = {
    (1 to dayCount).toList.map(x => (x, getDay(x).toString()))
  }

  private def getDay(n: Int): LocalDate = {
    LocalDate.ofYearDay(_year, n)
  }

  private def buildCountryHolidays: IndexedSeq[LocalDate] = {
    Vector(
      LocalDate.ofYearDay(2017, 9),
      LocalDate.ofYearDay(2017, 13),
      LocalDate.ofYearDay(2017, 17),
      LocalDate.ofYearDay(2017, 19)
    )
  }

  def buildCountryHolidayDate = {
    val countryHolidays = buildCountryHolidays

    for (
      i <- 1 to dayCount;
      d = LocalDate.ofYearDay(2017, i) if (countryHolidays contains d)
    ) yield HolidayDay(d)
  }

  def getWeekendDay(date: LocalDate): Option[MyDay] = {
    Option
      .when(PersonalCalendar.isWeekendDate(date))(WeekendDay(date))
  }

  def getHolidaysDay(
      date: LocalDate,
      holidays: List[LocalDate]
  ): Option[MyDay] = {
    Option
      .when(holidays.contains(date))(HolidayDay(date))
  }

  def getCountryHolidaysDay(
      date: LocalDate,
      holidays: List[LocalDate]
  ): Option[MyDay] = {
    Option
      .when(holidays.contains(date))(CountryHolidayDay(date))
  }

  def getAbsenceDay(
      date: LocalDate,
      absences: List[LocalDate]
  ): Option[MyDay] = {
    Option
      .when(absences.contains(date))(AbsenceDay(date))
  }

  def getWorkDay(date: LocalDate): Option[MyDay] = Option(WorkDay(date))
}
