package com.cmartin.data

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
        a1.date.compareTo(a2.date)
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

    private var repo = TreeSet[Aircraft]()

    override def getById(id: String): Try[Option[Aircraft]] = Try(repo.find(_.id == id))

    override def getAll(): Try[List[Aircraft]] = Try(repo.toList)

    override def getAll(f: Aircraft => Boolean): Try[List[Aircraft]] = Try(repo.filter(f).toList)

    override def remove(t: Aircraft): Try[Boolean] = Try(repo.remove(t))

    override def save(t: Aircraft): Try[Boolean] = {
      repo.find(_.id == t.id) match {
        case Some(a) => repo.remove(t) // remove before update
        case None => true
      }
      Try(repo.add(t)) // save or update
    }


    override def count(): Try[Int] = Try(repo.size)

    override def isEmpty(): Try[Boolean] = Try(repo.isEmpty)
  }

}
