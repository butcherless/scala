package com.cmartin.learn

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import org.json4s.JsonAST.{JArray, JField, JNothing, JObject, JString}
import org.json4s.native.JsonMethods
import org.json4s.{DefaultFormats, JValue}

import scala.annotation.tailrec
import scala.util.Try

object Json4sResearch {

  implicit val formats: DefaultFormats = org.json4s.DefaultFormats

  val payloadKey: String   = "payload"
  val stateKey: String   = "state"
  val timestampKey: String = "@timestamp"

  private val XPATH_REGEX = """([a-z][a-z0-9]*)+([.][a-z][a-z0-9]*)*""".r

  val dateTimeFormater = DateTimeFormatter.ISO_INSTANT

  def isXpath(path: String): Boolean =
    XPATH_REGEX.matches(path)

  def parse(jsonString: String): JValue =
    JsonMethods.parse(jsonString)

  def splitPath(path: String): Seq[String] = path.split('.').toList

  def getKey(path: String, json: JValue): JValue = {
    // loop function
    @tailrec
    def go(keys: Seq[String], json: JValue): JValue = {
      if (json.equals(JNothing) || keys.isEmpty)
        json
      else
        go(keys.tail, json \ keys.head)
    }

    if (path.isBlank) json
    else if (isXpath(path))
      go(splitPath(path), json)
    else
      throw new RuntimeException(s"invalid xpath: $path")
  }

  def flatten(json: JValue): JValue = {
    def _flatten(json: JValue, path: String = ""): JValue = {
      json match {
        case JObject(tuples) =>
          tuples
            .map {
              case (k, v) => _flatten(v, buildPath(path, k))
            }
            .fold(JNothing)(_ merge _)

        case JArray(jValues) => //throw ArrayNotSupportedException(ExceptionMessages.ARRAY_NOT_SUPPORTED)
          jValues.zipWithIndex
            .map {
              case (v, i) =>
                _flatten(v, buildPath(path, s"$i"))
            }
            .fold(JNothing)(_ merge _)

        case _ => JObject((path, json))
      }

    }

    _flatten(json)
  }

  /*
    validated date strings
   */
  def isNewer(current: String, last: String) = {
    ZonedDateTime.parse(current).compareTo(ZonedDateTime.parse(last)) > 0
  }

  def selectStringValue(key: String, json: JValue): List[String] = {
    for {
      JObject(fields)            <- json
      JField(`key`, JString(ts)) <- fields
    } yield ts
  }

  def getShadowTimestamp(json:JValue) : Either[Throwable, String] = {
    val seq = selectStringValue(timestampKey, json \ stateKey)
    if (seq.nonEmpty) Right(seq.head)
    else Left(new RuntimeException(s"missing key: $timestampKey"))
  }

  /*
    The timestamp is taken from the input message or in its absence
    the time in which the service processes the request.
   */
  def resolveTimestamp(json: JValue): Either[Throwable, String] = {
    val tsList = selectStringValue(timestampKey, json)
    if (tsList.isEmpty) Right(getNowDateText())
    else
      Try {
        ZonedDateTime.parse(tsList.head) // validate or fail
        tsList.head                      // is a valid date
      }.toEither
  }

  def createMetadata(json: JValue, dateText: String): JValue = {

    def go(json: JValue, path: String = ""): JValue = {
      json match {
        case JObject(tuples) =>
          tuples
            .map {
              case (k, v) => go(v, buildPath(path, k))
            }
            .fold(JNothing)(_ merge _)

        case JArray(jValues) => //throw ArrayNotSupportedException(ExceptionMessages.ARRAY_NOT_SUPPORTED)
          jValues.zipWithIndex
            .map {
              case (v, i) =>
                go(v, buildPath(path, s"$i"))
            }
            .fold(JNothing)(_ merge _)

        case _ =>
          JObject(
            (
              path,
              JObject(
                ("timestamp", JString(dateText))
              )
            )
          )
      }
    }

    go(json)
  }

  /*
    - gets the diff (changed, added, deleted) between current and incoming
    -
   */
  def mergeShadows(current: JValue, incoming: JValue): JValue = {
    val diff = current diff incoming
    current merge diff.changed merge diff.added
  }

  def createShadow(state: JValue, metadata: JValue): JValue =
    JObject(("state", state) :: ("metadata", metadata) :: Nil)

  def getNowDateText(): String =
    ZonedDateTime
      .now()
      .format(DateTimeFormatter.ISO_INSTANT)

  case class ArrayNotSupportedException(message: String) extends Exception(message)

  object ExceptionMessages {
    val ARRAY_NOT_SUPPORTED = "The parser doesn't support array type"
    val UNEXPECTED_TYPE     = "The type was not expected at this position of the document."
  }

}
