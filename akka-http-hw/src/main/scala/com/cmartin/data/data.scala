package com.cmartin

import java.time.LocalDate

import scala.collection.mutable.TreeSet
import scala.util.Try

package object data {

  case class Aircraft(id: String, typeCode: String, airline: String, desc: String, date: LocalDate)

  object Aircraft {
    implicit val ord = new Ordering[Aircraft] {

      /**
        * Comparator for dependencies classes
        *
        * @param c1 one dependency
        * @param c2 another one dependency
        * @return 0 if equals, -1 if less than, +1 if greater than
        */
      def compare(a1: Aircraft, a2: Aircraft): Int = {
        a1.id.compareTo(a2.id)
      }
    }
  }

  trait SimpleRepository[T] {

    /**
      *
      * @param id entity identifier
      * @return
      */
    def getById(id: String): Try[Option[T]]

    def getAll(): Try[List[T]]

    def getAll(f: (T) => Boolean): Try[List[T]]

    def remove(t: T): Try[Boolean]

    def removeAll(): Try[Unit]

    def save(t: T): Try[Boolean]

    /**
      *
      * @return
      */
    def count(): Try[Int]

    /**
      *
      * @return
      */
    def isEmpty(): Try[Boolean]
  }

  class AircraftRepository extends SimpleRepository[Aircraft] {

    private val repo = TreeSet[Aircraft]()

    override def getById(id: String): Try[Option[Aircraft]] = Try(repo.find(_.id == id))

    override def getAll(): Try[List[Aircraft]] = Try(repo.toList)

    override def getAll(f: Aircraft => Boolean): Try[List[Aircraft]] = Try(repo.filter(f).toList)

    override def remove(t: Aircraft): Try[Boolean] = Try(repo.remove(t))

    override def removeAll(): Try[Unit] = Try(repo.clear())

    override def save(ac: Aircraft): Try[Boolean] = {
      //TODO Try[Aircraft)
      repo.find(_.id == ac.id) match {
        case Some(a) => repo.remove(ac) // remove before update
        case None    => true
      }
      repo += ac
      Try(true) // save or update
    }

    override def count(): Try[Int] = Try(repo.size)

    override def isEmpty(): Try[Boolean] = Try(repo.isEmpty)

  }

}
