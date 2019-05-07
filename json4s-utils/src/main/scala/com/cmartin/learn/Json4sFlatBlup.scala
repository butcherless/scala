package com.cmartin.learn

import org.json4s.JsonAST.{JArray, JNothing, JObject}
import org.json4s.native.{JsonMethods, Serialization}
import org.json4s.{DefaultFormats, JValue}


/** Implementación de la funcionalidad para el tipo
  * [[https://docs.oracle.com/javase/8/docs/api/java/lang/String.html String]]
  *
  * Se utiliza para la interoperabilidad con aplicaciones Java. Utiliza el
  * formato JSON como interfaz.
  *
  * ==flatten==
  * Recibe como entrada un `JSON`.
  *
  * Obtiene como salida un `JSON` con la estructura de claves compuestas y de
  * un solo nivel formadas por cada uno de los path que forman las claves
  * de la entrada.
  *
  * ==blowup==
  * Recibe como entrada un `JSON` con la estructura de claves compuestas y de
  * un solo nivel de anidamiento.
  *
  * Obtiene como salida un `JSON` con las claves obtenidas de cada una de las
  * claves compuestas de la entrada.
  */
object Json4sFlatBlup extends FlatBlup[String, Option[String]] {

  implicit val formats: DefaultFormats = org.json4s.DefaultFormats
  /*
      sealed abstract class JValue
      case object JNothing extends JValue // 'zero' for JValue
      case object JNull extends JValue
      case class JString(s: String) extends JValue
      case class JDouble(num: Double) extends JValue
      case class JDecimal(num: BigDecimal) extends JValue
      case class JInt(num: BigInt) extends JValue
      case class JLong(num: Long) extends JValue
      case class JBool(value: Boolean) extends JValue
      case class JObject(obj: List[JField]) extends JValue
      case class JArray(arr: List[JValue]) extends JValue

      type JField = (String, JValue)
   */


  override def flatten(blownUp: String): Option[String] = {

    def _flatten(json: JValue, path: String = ""): JValue = {
      def flattenArray(elems: List[JValue]): JValue = {
        elems.size match {
          case 0 => JObject((path, JArray(Nil)) :: Nil)
          case _ => elems.zipWithIndex
            .map { case (v, i) => _flatten(v, buildArrayPath(path, i)) }
            .fold(JNothing)(_ merge _)
        }
      }

      json match {
        case JObject(tuples) => tuples
          .map { case (k, v) => _flatten(v, buildPath(path, k)) }
          .fold(JNothing)(_ merge _)

        case JArray(elems) => flattenArray(elems)

        case _ => JObject((path, json) :: Nil)
      }

    }

    /*
        flatten steps: {parse, flatten, serialize}
     */
    for {
      json: JValue <- JsonMethods.parseOpt(blownUp)
      flattened: JValue <- _flatten(json).toOption
      jsonString: String <- Option(Serialization.write(flattened))
    } yield jsonString
  }

  /*
  TODO
   */
  override def blowup(flatten: String): Option[String] = {

    def _blowup(json: JValue): JValue = {
      def blowupElem(keys: Array[String] = Array(), value: JValue): JValue = {
        if (keys.isEmpty) value
        else keys.head match {
          case ArrayElem(_) => //JArray(value :: Nil)
            JArray(blowupElem(keys.tail, value) :: Nil)
          case _ => JObject(keys.head -> blowupElem(keys.tail, value))
        }
      }

      json match {
        case JObject(tuples) => val r = tuples.sortWith(_._1 < _._1)
          .map(tuple => blowupElem(tuple._1.split('.'), tuple._2))

          r.fold(JNothing)(_ merge _)

        //TODO case JArray
        case _ => JNothing
      }
    }

    for {
      parsed: JValue <- JsonMethods.parseOpt(flatten)
      blownUp: JValue <- _blowup(parsed).toOption
      jsonString: String <- Option(Serialization.write(blownUp))
    } yield jsonString

  }

  private def buildPath(path: String, key: String): String =
    if (path.isEmpty) key else s"$path.$key"

  /*
    array size > 0
   */
  private def buildArrayPath(path: String, index: Int): String = {
    val elem = s"[$index]"
    if (path.isEmpty) elem else s"$path.$elem"
  }

  private val ArrayElem = """^\[(\d+)\]$""".r
}
