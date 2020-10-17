package com.cmartin.learn
import zio.console.Console
import zio.prelude.Validation
import zio.{App, ExitCode, UIO, URIO}

object ZioPreludePoc extends App {

  case class Message(key: String, json: String)

  def printMessage(m: String): URIO[Console, Unit] = zio.console.putStrLn(m)

  def validateKey(key: String): Validation[String, String] =
    if (key.nonEmpty) Validation.succeed(key)
    else Validation.fail("Empty key")

  def validateJson(json: String): Validation[String, String] =
    if (json.nonEmpty) Validation.succeed(json)
    else Validation.fail("Empty json")

  def validateJsonLength(json: String): Validation[String, String] =
    if (json.length % 2 == 0) Validation.succeed(json)
    else Validation.fail("Invalid json length")

  def validateJsonLower(json: String): Validation[String, String] =
    if (json.forall(_.isLower)) Validation.succeed(json)
    else Validation.fail("Invalid upper characters")

  def validateJsonLetters(json: String): Validation[String, String] =
    if (json.forall(_.isLetter)) Validation.succeed(json)
    else Validation.fail("Invalid digit characters")

  /* &> alias for zipParRight */
  def validateMessage(key: String, json: String): Validation[String, Message] =
    Validation.mapParN(
      validateKey(key),
      validateJson(json) &>
        validateJsonLength(json) &>
        validateJsonLower(json) &>
        validateJsonLetters(json)
    )(Message)

  /*
    key = "1234.5678"
    k1 = "1234" => 1234
    k2 = "5678" => 5678
    non-empty key
    pair key
    long key
   */

  def validateNonEmptyText(key: String): Validation[String, String] =
    Validation
      .fromPredicateWith("Empty key error", key)(_.nonEmpty)

  def validateKeyElements(key: String): Validation[String, (String, String)] = {
    val tuple: Array[String] = key.split('.')
    Validation
      .fromPredicateWith(s"Key must be a tuple: $key", tuple)(_.length == 2)
      .map(tuple => (tuple(0), tuple(1)))
  }

  def validateLongKey(key: String, name: String): Validation[String, Long] = {
    key.toLongOption
      .fold[Validation[String, Long]](Validation.fail(s"Key $name must be a number: $key"))(Validation.succeed)
  }

  def validateLongTuple(tuple: (String, String)): Validation[String, (Long, Long)] = {
    Validation
      .mapParN(validateLongKey(tuple._1, "left"), validateLongKey(tuple._2, "right"))((_, _))
  }

  def validateTupledKey(key: String): Validation[String, (Long, Long)] = {
    for {
      neKey  <- validateNonEmptyText(key)
      tKey   <- validateKeyElements(neKey)
      result <- validateLongTuple(tKey)
    } yield result
  }

  //TODO refactor to tests
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program = for {
      _           <- printMessage("hello zio prelude")
      validation1 <- UIO(validateMessage("key", "json"))
      _           <- printMessage(validation1.toString)
      validation2 <- UIO(validateMessage("", "abCd3"))
      _           <- printMessage(validation2.toString)
      validation3 <- UIO(validateTupledKey("1234.5678"))
      _           <- printMessage(validation3.toString)
      validation4 <- UIO(validateTupledKey(""))
      _           <- printMessage(validation4.toString)
      validation5 <- UIO(validateTupledKey("1.2.3"))
      _           <- printMessage(validation5.toString)
      validation6 <- UIO(validateTupledKey("abc.efg"))
      _           <- printMessage(validation6.toString)
      validation7 <- UIO(validateTupledKey("123.efg"))
      _           <- printMessage(validation7.toString)
      validation8 <- UIO(validateTupledKey("abc.456"))
      _           <- printMessage(validation8.toString)
      validation9 <- UIO(validateTupledKey("123.456"))
      _           <- printMessage(validation9.toString)
    } yield ()

    program.exitCode
  }
}
