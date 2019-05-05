package com.cmartin.learn.utils

/*
    String Refinements Type Class
 */
trait StringExtensions[A] {
  def removeSpaces(a: A): String
}

object StringExtensions {

  def removeSpaces[A: StringExtensions](a: A): String =
    StringExtensions[A].removeSpaces(a)

  def apply[A](implicit rs: StringExtensions[A]): StringExtensions[A] = rs

  implicit class StringExtensionsOps[A: StringExtensions](a: A) {
    def removeSpaces = StringExtensions[A].removeSpaces(a)
  }

  implicit val stringExt: StringExtensions[String] =
    (s: String) => s.replaceAll("[\\n\\s]", "")

}

