package com.cmartin.dto

case class Parent(id: Int, desc: String, children: Set[Child])
