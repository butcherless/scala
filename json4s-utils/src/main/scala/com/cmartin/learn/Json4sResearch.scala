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

  val EPOCH_TEXT = "1970-01-01T00:00:00Z"
  val excludeKey: String = "exclude"
  val metadataKey: String = "metadata"
  val payloadKey: String = "payload"
  val stateKey: String = "state"
  val tFieldKey: String = "t_field"
  val timestampKey: String = "@timestamp"

  private val XPATH_REGEX = """([a-z][a-z0-9]*)+([.][a-z][a-z0-9]*)*""".r

  val dateTimeFormater: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

  def isXpath(path: String): Boolean =
    XPATH_REGEX.matches(path)

  def parse(jsonString: String): JValue =
    JsonMethods.parse(jsonString)

  def splitPath(path: String): List[String] = path.split('.').toList

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

  @tailrec
  def excludeKeys(keys: List[String], json: JValue): JValue =
    keys match {
      case head :: tail =>
        excludeKeys(tail, json.replace(splitPath(head), JNothing))

      case Nil => json.remove(_ == JNothing)
    }

  def flatten(json: JValue): JValue = {
    def _flatten(json: JValue, path: String = ""): JValue = {
      json match {
        case JObject(tuples) =>
          tuples
            .map { case (k, v) =>
              _flatten(v, buildPath(path, k))
            }
            .fold(JNothing)(_ merge _)

        case JArray(
              jValues
            ) => // throw ArrayNotSupportedException(ExceptionMessages.ARRAY_NOT_SUPPORTED)
          jValues.zipWithIndex
            .map { case (v, i) =>
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
  def isNewer(current: String, last: String): Boolean = {
    ZonedDateTime.parse(current).compareTo(ZonedDateTime.parse(last)) > 0
  }

  def getExclusionKeys(metadata: JValue): List[String] = {
    metadata \ excludeKey match {
      case JArray(as) =>
        as.collect { // ignores JValue types except JString
          case JString(text) => text
        }
      case _ => List.empty
    }
  }

  def getShadowTimestamp(json: JValue): Either[Throwable, String] = {
    getStringValue(timestampKey, json \ stateKey)
      .fold(Right(EPOCH_TEXT))(Right(_))
  }

  /*
    The timestamp is taken from the input message or in its absence
    the time in which the service processes the request.
    - 1. payload.@timestamp
    - 2. metadata.t_field and then payload.t_field_value
    - 3. now()
    - errors ...
   */
  def resolveTimestamp(json: JValue): Either[Throwable, String] = {
    // validation function, parse date or fail
    def dateTextToEither(dateText: String): Either[Throwable, String] = {
      Try {
        ZonedDateTime.parse(dateText) // validate or fail
        dateText // is a valid date
      }.toEither
    }

    // new
    getStringValue(tFieldKey, json \ metadataKey) match {
      case Some(tfValue) =>
        getStringValue(tfValue, json \ payloadKey)
          .fold[Either[Throwable, String]](
            Left(new RuntimeException("t_field not found"))
          )(tsValue => dateTextToEither(tsValue))

      case None => // no t_field
        getStringValue(timestampKey, json \ payloadKey)
          .fold[Either[Throwable, String]](
            Right(formatNowDateText())
          )(tsValue => dateTextToEither(tsValue))
    }
  }

  def filterPayload(
      payload: JValue,
      keys: List[String]
  ): Either[Throwable, JValue] = {
    Right(
      if (keys.isEmpty) {
        payload
      } else {
        excludeKeys(keys, payload)
      }
    )
  }

  /*
    Traverses the tree to the leaf nodes (recursive)
    building the flatten path with the visited keys (k1.k2.k3...)
    and adding the timestamp attribute on the leaf (k1.k2.k3: timestamp)
   */
  def createMetadata(json: JValue, dateText: String): JValue = {

    def go(json: JValue, path: String = ""): JValue = {
      json match {
        case JObject(tuples) =>
          tuples
            .map { case (k, v) =>
              go(v, buildPath(path, k))
            }
            .fold(JNothing)(_ merge _)

        case JArray(
              jValues
            ) => // throw ArrayNotSupportedException(ExceptionMessages.ARRAY_NOT_SUPPORTED)
          jValues.zipWithIndex
            .map { case (v, i) =>
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
    - merges the current with the changes and the additions from the incoming
   */
  def mergeShadows(current: JValue, incoming: JValue): JValue = {
    val diff = current diff incoming
    current merge diff.changed merge diff.added
  }

  def createShadow(state: JValue, metadata: JValue): JValue =
    JObject(
      List(
        ("state", state),
        ("metadata", metadata)
      )
    )

  private def getStringValue(key: String, json: JValue): Option[String] = {
    val fields = for {
      JObject(fields) <- json
      JField(`key`, JString(ts)) <- fields
    } yield ts

    fields.headOption
  }

  private def formatNowDateText(): String =
    ZonedDateTime
      .now()
      .format(DateTimeFormatter.ISO_INSTANT)

  case class ArrayNotSupportedException(message: String)
      extends Exception(message)

  object ExceptionMessages {
    val ARRAY_NOT_SUPPORTED = "The parser doesn't support array type"
    val UNEXPECTED_TYPE =
      "The type was not expected at this position of the document."
  }

}
