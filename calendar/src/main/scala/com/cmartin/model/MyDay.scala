package com.cmartin.model

import java.time.LocalDate

/**
  * Created by cmartin on 29/01/2017.
  */
sealed trait MyDay {
  def localDate: LocalDate
}

final case class AbsenceDay(localDate: LocalDate) extends MyDay

final case class CountryHolidayDay(localDate: LocalDate) extends MyDay

final case class HolidayDay(localDate: LocalDate) extends MyDay

final case class WeekendDay(localDate: LocalDate) extends MyDay

final case class WorkDay(localDate: LocalDate) extends MyDay


