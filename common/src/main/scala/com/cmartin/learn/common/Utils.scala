package com.cmartin.learn.common

object Utils {

  def prettyPrint[T](list: List[T]) : String =
    list.mkString(s"list size (${list.size}) =>\n[\n",",\n","\n]")
}
