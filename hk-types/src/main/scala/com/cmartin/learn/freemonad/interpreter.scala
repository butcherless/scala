package com.cmartin.learn.freemonad

import cats.{Id, ~>}
import com.cmartin.learn.freemonad.algebra.{Create, CrudOperationA, Delete, Read, Update}
import com.cmartin.learn.freemonad.functions.buildCryptoCurrency

import scala.concurrent.Future

object interpreter {

  // 5. Build the program compiler
  //val compiler: CrudOperationA ~> Id = ???
  //val compiler: CrudOperationA ~> Option = ???
  //val compiler: CrudOperationA ~> Either = ???
  //val compiler: CrudOperationA ~> Future = ???

  def idCompiler: CrudOperationA ~> Id =
    new (CrudOperationA ~> Id) {
      def apply[A](fa: CrudOperationA[A]): Id[A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Id: $cc")
            cc.name
          case Read(name) =>
            println(s"read name Id: $name")
            buildCryptoCurrency(name)
          case Update(cc) =>
            println(s"update crypto currency Id: ${cc}")
            cc
          case Delete(cc) =>
            println(s"delete crypto currency Id: ${cc.id}")
            cc.id
        }
    }

  def optionCompiler: CrudOperationA ~> Option =
    new (CrudOperationA ~> Option) {
      override def apply[A](fa: CrudOperationA[A]): Option[A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Option: $cc")
            if (cc.id == constants.foundUuid) None
            else Some(cc.name)
          case Read(name) =>
            println(s"read name Option: $name")
            if (name == constants.notFoundName) None
            else Some(buildCryptoCurrency(name))
          case Update(cc) =>
            println(s"update crypto currency Option: ${cc}")
            if (cc.id == constants.notFoundUuid) None
            else Some(cc)
          case Delete(cc) =>
            println(s"delete crypto currency Option: ${cc.id}")
            if (cc.id == constants.notFoundUuid) None
            else Some(cc.id)
        }
    }

  //TODO rename
  type SingleEither[A] = Either[String, A]

  def eitherCompiler: CrudOperationA ~> SingleEither =
    new (CrudOperationA ~> SingleEither) {
      override def apply[A](fa: CrudOperationA[A]): Either[String, A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Either: $cc")
            if (cc.id == constants.foundUuid) Left(constants.operationErrorMessage)
            else Right(cc.name)
          case Read(name) =>
            println(s"read name Either: $name")
            if (name == constants.notFoundName) Left(constants.operationErrorMessage)
            else Right(buildCryptoCurrency(name))
          case Update(cc) =>
            println(s"update crypto currency Either: ${cc}")
            if (cc.id == constants.notFoundUuid) Left(constants.operationErrorMessage)
            else Right(cc)
          case Delete(cc) =>
            println(s"delete crypto currency Either: ${cc.id}")
            if (cc.id == constants.notFoundUuid) Left(constants.operationErrorMessage)
            else Right(cc.id)
        }
    }

  def futureCompiler: CrudOperationA ~> Future =
    new (CrudOperationA ~> Future) {
      override def apply[A](fa: CrudOperationA[A]): Future[A] =
        fa match {
          case Create(cc) =>
            println(s"create crypto currency Future: ${cc}")
            if (cc.id == constants.foundUuid)
              Future.failed(new RuntimeException(constants.operationErrorMessage))
            else Future.successful(cc.name)
          case Read(name) =>
            println(s"read name Future: $name")
            if ((name == constants.notFoundName))
              Future.failed(new RuntimeException(constants.operationErrorMessage))
            else Future.successful(buildCryptoCurrency(name))
          case Update(cc) =>
            println(s"update crypto currency Future: ${cc}")
            if (cc.id == constants.notFoundUuid)
              Future.failed(new RuntimeException(constants.operationErrorMessage))
            else Future.successful(cc)
          case Delete(cc) =>
            println(s"delete crypto currency Future: ${cc.id}")
            if (cc.id == constants.notFoundUuid)
              Future.failed(new RuntimeException(constants.operationErrorMessage))
            else Future.successful(cc.id)
        }
    }

}
