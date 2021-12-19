package com.cmartin.learn

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
    (s: String) => s.replaceAll("""[\n\s]""", "")

}

object TestSamples {

  import com.cmartin.learn.StringExtensions.StringExtensionsOps

  val nestedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2": {
      |    "k21": 2.0,
      |    "k22" : 3
      |  },
      |  "k3": null,
      |  "k4": {
      |    "k41": {
      |      "k411": "value-5",
      |      "k412": true
      |    },
      |    "k42": "value-6",
      |    "k43": [1,2,3],
      |    "k44": []
      |  }
      |}
    """.stripMargin.removeSpaces

  val flattenedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2.k21": 2.0,
      |  "k2.k22": 3,
      |  "k3": null,
      |  "k4.k41.k411": "value-5",
      |  "k4.k41.k412": true,
      |  "k4.k42": "value-6",
      |  "k4.k43.[0]": 1,
      |  "k4.k43.[1]": 2,
      |  "k4.k43.[2]": 3,
      |  "k4.k44": []
      |}
    """.stripMargin.removeSpaces

  val nestedArrayJson: String =
    """
      |[
      |{
      |  "k1": "value-1",
      |  "k2": {
      |    "k21": 2.0,
      |    "k22" : 3
      |  },
      |  "k3": null
      |},
      |{
      |  "k4": {
      |    "k41": {
      |      "k411": "value-5",
      |      "k412": true
      |    }
      |  }
      |}
      |]
    """.stripMargin.removeSpaces

  val flattenedArrayJson: String =
    """
      |{
      |"[0].k1": "value-1",
      |"[0].k2.k21": 2.0,
      |"[0].k2.k22": 3,
      |"[0].k3": null,
      |"[1].k4.k41.k411": "value-5",
      |"[1].k4.k41.k412": true
      |}
    """.stripMargin.removeSpaces

  val invalidNestedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2": {
      |    "k21": 9.99,
      |    "k22" : 9
      |  },
      |  "k3": null,
      |  "k4": {
      |    "k41": {
      |      "k411": INVALID,
      |      "k412": true
      |    },
      |    "k42": "value-6"
      |  }
      |}
    """.stripMargin.removeSpaces

  val invalidFlattenedJson: String =
    """
      |{
      |  "k1": "value-1",
      |  "k2.k21": 2.0,
      |  "k2.k22": 3,
      |  "k3": null,
      |  "k4.k41.k411": "value-5",
      |  "k4.k41.k412": true,
      |  "k4.k42": "value-6,
      |  "k4.k43.[0]": 1,
      |  "k4.k43.[1]": 2,
      |  "k4.k43.[2]": 3
      |}
    """.stripMargin.removeSpaces
}
