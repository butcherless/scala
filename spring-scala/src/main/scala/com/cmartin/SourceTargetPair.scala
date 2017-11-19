package com.cmartin

case class SourceTargetPair(source:String,target:String,limit:Int) {

  override def toString: String = {
    val classname = SourceTargetPair.getClass.getSimpleName

    s"$classname[source=$source, target=$target, limit=$limit], $SourceTargetPair.getClass.getSimpleName"
  }
}
