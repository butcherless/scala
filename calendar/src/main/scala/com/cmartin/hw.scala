package com.cmartin

import java.time.LocalDate

import scalaz.Scalaz._
//import std.option._, std.list._ // functions and type class instances for Option and List

object HelloWorld extends App {

  trait Monoid[A] {
    def mappend(a1: A, a2: A): A

    def mzero: A
  }

  object Monoid {
    implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
      def mappend(a: Int, b: Int): Int = a + b

      def mzero: Int = 0
    }
    implicit val StringMonoid: Monoid[String] = new Monoid[String] {
      def mappend(a: String, b: String): String = a + b

      def mzero: String = ""
    }
  }

  def sum[A: Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    xs.foldLeft(m.mzero)(m.mappend)
  }

  def createOptionInt = {
    7.some
  }

  def equals(a: Int, b: Int): Boolean = {
    a === b
  }

  def getVowelList = {
    'a' |-> 'e'
  }

  val YEAR = 2017

  println("Scala Hello World")

  println(createOptionInt)

  // type class monoid for Int
  println(sum(List(1, 2, 3, 4)))

  // type class monoid for String
  println(sum(List("He", "llo", " ", "Wor", "ld")))

  // returns true
  println(equals(1, 1))

  println(getVowelList)

  val year = new LocalDateCalendar(YEAR)

  println("year day count: " + year.dayCount)

  val list = List("2017-02-08", "2017-02-09", "2017-02-10")

  println(list.map(x => (LocalDate.parse(x).getClass, x)))

  println(year.buildCountryHolidayDate)

  year.getYearDays.map(x => println(x, x.localDate.getDayOfYear))
}
