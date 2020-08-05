import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

object LogicSpec extends AnyFlatSpec with Matchers {

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

}
