package com.cmartin.learn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import MyLibrary._
import cats.instances.tailRec

class LogicSpec extends AnyFlatSpec with Matchers {

  import Logic.subtractOne

  it should "be 100% when all attributes match" in {
    val tabby  = Kitten(1, List("male", "tabby"))
    val prefs  = BuyerPreferences(List("male", "tabby"))
    val result = Logic.matchLikelihood(tabby, prefs)
    result shouldBe >(.999)
  }

  it should "be 0% when no attributes match" in {
    val tabby  = Kitten(1, List("male", "tabby"))
    val prefs  = BuyerPreferences(List("female", "calico"))
    val result = Logic.matchLikelihood(tabby, prefs)
    result shouldBe <(.001)
  }

  it should "test main case" in {
    val result = for {
      r1 <- subtractOne(3)
      r2 <- subtractOne(r1)
      r3 <- subtractOne(r2)
    } yield r3
    result.isSuccess shouldBe true
    result.get shouldBe 0
  }

  it should "test error case" in {
    val result = for {
      r1 <- subtractOne(1)
      r2 <- subtractOne(r1)
      r3 <- subtractOne(r2)
    } yield r3
    result.isFailure shouldBe true
  }

  /* - optimize loop
   */
  def multiply(a: Int, b: Int): Int = {

    def loop(a: Int, b: Int): Int = {
      if (a > 0) b + loop(a - 1, b)
      else 0
    }

    if (a <= b) loop(a, b)
    else loop(b, a)
  }

  it should "TODO multiply two integers case 1" in {
    val a   = 2
    val b   = 5
    val res = multiply(a, b)

    res shouldBe a * b
  }

  it should "TODO multiply two integers case 2" in {
    val a   = 5
    val b   = 2
    val res = multiply(a, b)

    res shouldBe a * b
  }

  it should "TODO multiply two integers case 3" in {
    val a   = 1
    val b   = 5
    val res = multiply(a, b)

    res shouldBe a * b
  }

  it should "TODO multiply two integers case 4" in {
    val a   = 0
    val b   = 5
    val res = multiply(a, b)

    res shouldBe a * b
  }

  it should "TODO multiply two integers case 5" in {
    val a   = 5
    val b   = 0
    val res = multiply(a, b)

    res shouldBe a * b
  }

  it should "TODO multiply two integers case 6" in {
    val a   = 2
    val b   = 2
    val res = multiply(a, b)

    res shouldBe a * b
  }

}
