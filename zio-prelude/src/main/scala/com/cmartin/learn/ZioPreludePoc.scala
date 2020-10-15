package com.cmartin.learn
import zio.prelude.Validation
import zio.{App, ExitCode, UIO, URIO}

object ZioPreludePoc extends App {

  case class Message(key: String, json: String)

  def printMessage(m: String) = zio.console.putStrLn(m)

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

  def validateMessage(key: String, json: String): Validation[String, Message] =
    Validation.mapParN(
      validateKey(key),
      validateJson(json)
        .zipParRight(validateJsonLength(json))
        .zipParRight(validateJsonLower(json))
        .zipParRight(validateJsonLetters(json))
    )(Message)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program = for {
      _           <- printMessage("hello zio prelude")
      validation1 <- UIO(validateMessage("key", "json"))
      _           <- printMessage(validation1.toString)
      validation2 <- UIO(validateMessage("", "abCd3"))
      _           <- printMessage(validation2.toString)
    } yield ()

    program.exitCode
  }
}
