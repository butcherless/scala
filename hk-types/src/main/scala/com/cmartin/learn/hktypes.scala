package com.cmartin.learn

import java.util.UUID

import com.cmartin.learn.functions._
import com.cmartin.learn.types.MyMap

import scala.util.{Failure, Success, Try}

object main {
  //extends App {

  val s = ApiImpl.read(7)
  println(s"Generic: $s")

  val r1 = HktOption.read(1)
  println(s"HktOption: $r1")

  val r2 = HktList.read(2)
  println(s"HktList: $r2, ${r2.head}")

  val r3 = HktMap.read(3)
  println(s"HktMap: $r3, ${r3(3)}")

  // option implementation
  val optionService: CoinMarketService[Option] = new OptionCoinMarketService(
    new CrytoCurrencyRepository
  )

  val resultNone = optionService.readByName("Dummy")
  printOption(resultNone)

  val resultSome = optionService.readByName(("Bitcoin"))
  printOption(resultSome)

  // try implementation
  val tryService: CoinMarketService[Try] = new TryCoinMarketService(new CrytoCurrencyRepository)
  val resultFailure = tryService.readByName("Dummy")
  printTry(resultFailure)

  val resultSuccess = tryService.readByName(("TRON"))
  printTry(resultSuccess)

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

trait HktApi[F[_]] {
  def read(n: Long): F[String]
}

object HktOption extends HktApi[Option] {
  def read(n: Long): Option[String] = Option(s"String of $n")
}

object HktList extends HktApi[List] {
  def read(n: Long): List[String] = List(s"String of $n")
}

object HktMap extends HktApi[MyMap] {
  def read(n: Long) = Map(n -> s"string: $n.toString")
}

case class ValidationError(message: String)

// S E R V I C E   D E F I N I T I O N

trait CoinMarketService[C[_]] {
  def create(cc: CryptoCurrency): C[CryptoCurrency]

  def update(cc: CryptoCurrency): C[CryptoCurrency]

  def delete(cc: CryptoCurrency): C[UUID]

  def readById(id: UUID): C[CryptoCurrency]

  def readByName(name: String): C[CryptoCurrency]

  def readAll(): C[List[CryptoCurrency]]
}

// S E R V I C E   I M P L E M E N T A T I O N S

class OptionCoinMarketService(repo: CrytoCurrencyRepository) extends CoinMarketService[Option] {
  override def create(cc: CryptoCurrency): Option[CryptoCurrency] =
    Factory.newOptionCrytoCurrency(cc.name, cc.marketCap, cc.price)

  override def update(cc: CryptoCurrency): Option[CryptoCurrency] = ???

  override def delete(cc: CryptoCurrency): Option[UUID] = ???

  override def readById(id: UUID): Option[CryptoCurrency] = ???

  override def readByName(name: String): Option[CryptoCurrency] = repo.getByName(name)

  override def readAll(): Option[List[CryptoCurrency]] = ???
}

case class ServiceException(message: String) extends RuntimeException(message)

class TryCoinMarketService(repo: CrytoCurrencyRepository) extends CoinMarketService[Try] {
  override def create(cc: CryptoCurrency): Try[CryptoCurrency] =
    Factory.newTryCrytoCurrency(cc.name, cc.marketCap, cc.price)

  override def update(cc: CryptoCurrency): Try[CryptoCurrency] = ???

  override def delete(cc: CryptoCurrency): Try[UUID] = ???

  override def readById(id: UUID): Try[CryptoCurrency] = ???

  override def readByName(name: String): Try[CryptoCurrency] = repo.getByName(name) match {
    case Some(cc) => Success(cc)
    case None => Failure(ServiceException(s"Currency not found [${name}]"))
  }

  override def readAll(): Try[List[CryptoCurrency]] = ???
}
