package com.cmartin.learn

object main extends App {

  val s = ApiImpl.read(7)
  println(s"Generic: $s")


  val r1 = HktOption.read(1)
  println(s"HktOption: $r1")

  val r2 = HktList.read(2)
  println(s"HktList: $r2, ${r2.head}")

  val r3 = HktMap.read(3)
  println(s"HktMap: $r3, ${r3(3)}")

}

object types {
  type MyMap[String] = Map[Long, String]
}


trait Api[T[String]] {
  def read(n: Long): T[String]
}

object ApiImpl extends Api[Option] {
  def read(n: Long): Option[String] = (n > 0) match {
    case true => Option(n.toString)
    case _ => None
  }
}

/*
object BananaApiImpl extends Api[Banana] {
  def read(n:Long) : Banana = {
    if (n > 0) Banana("good")
    else Banana("bad")
  }
}
*/

trait HktApi[F[_]] {
  def read(n: Long): F[String]
}

object HktOption extends HktApi[Option] {
  def read(n: Long): Option[String] = Option(s"String of $n")
}

object HktList extends HktApi[List] {
  def read(n: Long): List[String] = List(s"String of $n")
}

import java.util.UUID

import com.cmartin.learn.types.MyMap

object HktMap extends HktApi[MyMap] {
  def read(n: Long) = Map(n -> s"string: $n.toString")
}


case class Banana(rate: String)

case class CrytoCurrency(id: UUID, name: String, marketCap: BigDecimal, price: BigDecimal, change: Double = 0.0)

object CrytoCurrency {
  implicit val ord = new Ordering[CrytoCurrency] {
    /**
      * Comparator for dependencies classes
      *
      * @param c1 one dependency
      * @param c2 another one dependency
      * @return 0 if equals, -1 if less than, +1 if greater than
      */
    def compare(c1: CrytoCurrency, c2: CrytoCurrency): Int = {
      c1.name.compareTo(c2.name)
    }
  }
}

trait CoinMarketService[C[_]] {
  def create(cc: CrytoCurrency): C[CrytoCurrency]

  def update(cc: CrytoCurrency): C[CrytoCurrency]

  def delete(cc: CrytoCurrency): C[UUID]

  def readById(id: UUID): C[CrytoCurrency]

  def readByName(name: String): C[CrytoCurrency]

  def readAll(): C[List[CrytoCurrency]]
}

class OptionCoinMarketService(repo: CrytoCurrencyRepository) extends CoinMarketService[Option] {
  override def create(cc: CrytoCurrency): Option[CrytoCurrency] = Factory.newCrytoCurrency(cc.name, cc.marketCap, cc.price)

  override def update(cc: CrytoCurrency): Option[CrytoCurrency] = ???

  override def delete(cc: CrytoCurrency): Option[UUID] = ???

  override def readById(id: UUID): Option[CrytoCurrency] = ???

  override def readByName(name: String): Option[CrytoCurrency] = repo.getByName(name)

  override def readAll(): Option[List[CrytoCurrency]] = ???
}