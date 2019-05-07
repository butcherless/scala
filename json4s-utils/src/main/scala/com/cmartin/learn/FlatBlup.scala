package com.cmartin.learn

/** Trait para convertir un `Path` contenido en el tipo de entrada en una `Key`
  * compuesta contenida en el tipo de salida. La `Key` está compuesta por cada
  * uno de los elementos del `Path` y viceversa.
  *
  * @tparam I tipo de entrada para las operaciones
  * @tparam O tipo de salida para las operaciones
  */
trait FlatBlup[I, O] {

  /** Convierte el Path contenido en el tipo de entrada en una clave compuesta
    * en el tipo de salida.
    *
    * @param blownUp tipo de entrada con el `Path``
    *               @return tipo de salida con la `Key` compuesta
    * */
  def flatten(blownUp: I): O

  def flattenToMap(blownUp: I): Option[Map[String, _]]

  /** Convierte la `Key` compuesta en el tipo de entrada en un `Path` contenido
    * en el tipo de salida.
    *
    * @param flatten tipo de entrada con la `Key`
    * @return tipo de salida con el `Path`
    */
  def blowup(flatten: I): O

}
