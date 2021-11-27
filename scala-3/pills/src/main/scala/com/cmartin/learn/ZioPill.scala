package com.cmartin.learn

import zio.{IO, Runtime, Task, ULayer, ZIO, ZLayer}

import java.time.{Instant, LocalDateTime}
import java.util.UUID

object ZioPill {
  val runtime = Runtime.default

  sealed trait BaseError {
    val message: String
  }

  sealed trait RepositoryError extends BaseError

  case class MissingEntityError(message: String) extends RepositoryError

  case class DuplicateEntityError(message: String) extends RepositoryError

  sealed trait ViewError extends BaseError

  case class NotFoundError(message: String) extends ViewError

  case class BadRequestError(message: String) extends BaseError

  case class UnknownError(message: String) extends BaseError

  def manageError(e: BaseError): String = e match {
    case MissingEntityError(_) => "missing"
    case NotFoundError(_)      => "not-found"
    case UnknownError(_)       => "unknown"
    case _                     => "default"
  }

  def manageRepositoryError(e: RepositoryError): String = e match {
    case MissingEntityError(_)   => "missing"
    case DuplicateEntityError(_) => "duplicate"
  }

  case class Location(lon: Double, lat: Double)
  case class Address(name: String, number: Option[Int])
  case class MessageDbo(
      id: UUID,
      date: Instant,
      data: String,
      location: Location
  )
  /*
  object MessageRepository {
    type MessageRepositoryEnv = Has[MessageRepository.Service]

    trait Service {
      def findById(id: UUID): IO[RepositoryError, MessageDbo]
    }

    val live: ULayer[MessageRepositoryEnv] =
      ZLayer.succeed(
        new Service {
          override def findById(id: UUID): IO[RepositoryError, MessageDbo] =
            IO.succeed(
              MessageDbo(id, Instant.now, "message-data", Location(3.0, 40.0))
            )
        }
      )

    def findById(
        id: UUID
    ): ZIO[MessageRepositoryEnv, RepositoryError, MessageDbo] =
      ZIO.accessM(_.get.findById(id))
  }


  object AddressService {
    type AddressServiceEnv = Has[AddressService.Service]

    trait Service {
      def findByLocation(location: Location): IO[ViewError, Address]
    }

    val live: ULayer[AddressServiceEnv] =
      ZLayer.succeed(
        new Service {
          override def findByLocation(
              location: Location
          ): IO[ViewError, Address] =
            IO.succeed(Address("address-name", Some(1)))
        }
      )

    def findByLocation(
        location: Location
    ): ZIO[AddressServiceEnv, ViewError, Address] =
      ZIO.accessM(_.get.findByLocation(location))
  }

  def findAddressByMessageId(id: UUID) =
    for {
      message <- MessageRepository.findById(id)
      address <- AddressService.findByLocation(message.location)
    } yield (address)

  val messageAddressLayer = MessageRepository.live ++ AddressService.live

  val result: IO[BaseError, Address] =
    findAddressByMessageId(UUID.randomUUID)
      .provideLayer(messageAddressLayer)
   */
}
