package com.cmartin.learn.generator

case class TypeRepresentation(t: Type, f: File) {
  def withPackage(name: String) = {
    // assing param
    this
  }

  def withStringBuilder(sb: ToStringBuilder) = {
    this
  }
}
