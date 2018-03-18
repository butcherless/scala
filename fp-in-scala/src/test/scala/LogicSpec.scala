import org.specs2.mutable.Specification

object LogicSpec extends Specification {

  import Logic.subtractOne

  "The 'matchLikelihood' method" should {
    "be 100% when all attributes match" in {
      val tabby = Kitten(1, List("male", "tabby"))
      val prefs = BuyerPreferences(List("male", "tabby"))
      val result = Logic.matchLikelihood(tabby, prefs)
      result must beGreaterThan(.999)
    }
  }

  "The 'matchLikelihood' method" should {
    "be 0% when no attributes match" in {
      val tabby = Kitten(1, List("male", "tabby"))
      val prefs = BuyerPreferences(List("female", "calico"))
      val result = Logic.matchLikelihood(tabby, prefs)
      result must beLessThan(.001)
    }
  }

  "test main case" >> {
    val result = for {
      r1 <- subtractOne(3)
      r2 <- subtractOne(r1)
      r3 <- subtractOne(r2)
    } yield r3
    result.isSuccess must beTrue
    result.get must beEqualTo(0)
  }

  "test error case" >> {
    val result = for {
      r1 <- subtractOne(1)
      r2 <- subtractOne(r1)
      r3 <- subtractOne(r2)
    } yield r3
    result.isFailure must beTrue
  }

}