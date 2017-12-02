package com.cmartin.learn.generator

sealed trait ToStringBuilder {

  case object StringBuilder extends ToStringBuilder

  case object CommonsLang3 extends ToStringBuilder

}
