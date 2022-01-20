package com.cmartin.learn

package object util {

  def copy(map: Map[String, Any]): Map[String, Any] = {

    def go(
        map: Map[String, Any],
        res: Map[String, Any] = Map.empty[String, Any]
    ): Map[String, Any] = {
      if (map.isEmpty)
        res
      else {
        map.head match {
          case (k: String, v: Map[String, Any] @unchecked) =>
            go(map.tail, res + (k -> go(v)))

          case (k: String, v: Any) => go(map.tail, res + (k -> v))
        }
      }
    }

    go(map)
  }

  def debug(map: Map[String, Any]): Unit = {
    println(s"map size: ${map.size}, type: ${map.getClass}")
    if (map.nonEmpty)
      map.head match {
        case (k: String, v: Map[String, Any] @unchecked) =>
          println(s"key: ${k}, value map")
          debug(v)
          debug(map.tail)
        case (k: String, v: Any)                         =>
          println(s"key: ${k}, value: ${v}, type: ${v.getClass}")
          debug(map.tail)
      }
  }
}
