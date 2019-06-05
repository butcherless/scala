package com.cmartin.learn

import org.json4s.JValue
import org.json4s.JsonAST.{ JArray, JObject }
import org.json4s.native.JsonMethods

object MapFlatBlup extends FlatBlup[String, Option[Map[String, Any]]] {

  override def flatten(blownUp: String): Option[Map[String, Any]] = {

    def _flatten(json: JValue, path: String = ""): Map[String, Any] = {
      def flattenArray(elems: List[JValue]): Map[String, Any] = {
        elems.size match {
          case 0 => Map(path -> Nil)
          case _ => elems.zipWithIndex
            .map { case (v, i) => _flatten(v, buildArrayPath(path, i)) }
            .fold(Map[String, Any]())(_ ++ _)
        }
      }

      json match {
        case JObject(tuples) => tuples
          .map { case (k, v) => _flatten(v, buildPath(path, k)) }
          .fold(Map[String, Any]())(_ ++ _)

        case JArray(elems) => flattenArray(elems)

        case _ => Map(path -> json.extract[Any])
      }

    }

    /*
        flatten steps: {parse, flatten, toMap}
     */
    for {
      json: JValue <- JsonMethods.parseOpt(blownUp)
      flattened: Map[String, Any] <- Option(_flatten(json))
    } yield flattened
  }

  /**
   * Convierte la `Key` compuesta en el tipo de entrada en un `Path` contenido
   * en el tipo de salida.
   *
   * @param flatten tipo de entrada con la `Key`
   * @return tipo de salida con el `Path`
   */
  override def blowup(flatten: String): Option[Map[String, Any]] = ???
}
