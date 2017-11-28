package com.cmartin

import scala.beans.BeanProperty

case class SourceTargetPair(@BeanProperty source: String,
                            @BeanProperty target: String,
                            @BeanProperty limit: Int)
