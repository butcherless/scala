package com.cmartin.utils.poc

import com.cmartin.utils.poc.Zio2Layers.Infrastructure.{Config, ConfigLive, Database, DatabaseLive}
import com.cmartin.utils.poc.Zio2Layers.Model.{Aircraft, AuditCounters}
import com.cmartin.utils.poc.Zio2Layers.Repositories.{
  AircraftRepository,
  AircraftRepositoryLive,
  AirlineRepository,
  AirlineRepositoryLive
}
import com.cmartin.utils.poc.Zio2Layers.Services.{AuditService, AuditServiceLive}
import zio.{IO, ZIO, ZIOAppDefault, _}

object Zio2Layers
    extends ZIOAppDefault {

  object Model {
    case class Airline(name: String, code: String)

    case class Aircraft(registration: String)

    case class AuditCounters(airline: Int, aircraft: Int)
  }

  object Infrastructure {
    case class ApplicationConfig(logLevel: String)

    case class QueryResults()

    trait Config {
      def read(filepath: String): IO[String, ApplicationConfig]
    }

    case class ConfigLive() extends Config {
      override def read(filepath: String): IO[String, ApplicationConfig] =
        ZIO.succeed(ApplicationConfig("INFO"))
    }

    object ConfigLive {
      val layer: ULayer[Config] =
        ZLayer.succeed(ConfigLive())
    }

    trait Database {
      def doQuery(query: String): IO[String, QueryResults]
    }

    case class DatabaseLive() extends Database {
      override def doQuery(query: String): IO[String, QueryResults] =
        ZIO.succeed(QueryResults()) // use Slick or Quill
    }

    object DatabaseLive {
      val layer: ULayer[Database] =
        ZLayer.succeed(DatabaseLive())
    }
  }

  object Repositories {

    import Infrastructure.Database

    trait AirlineRepository {
      def count(): IO[String, Int]
    }

    case class AirlineRepositoryLive(database: Database) extends AirlineRepository {
      override def count(): IO[String, Int] = for {
        _ <- database.doQuery("dummy query")
      } yield 10
    }

    object AirlineRepositoryLive {
      val layer: URLayer[Database, AirlineRepository] =
        ZLayer.fromFunction(db => AirlineRepositoryLive(db))
    }

    trait AircraftRepository {
      def count(): IO[String, Int]

      def countByAirline(code: String): IO[String, Int]

      def findByRegistration(registration: String): IO[String, Aircraft]
    }

    case class AircraftRepositoryLive(database: Database) extends AircraftRepository {

      def findByRegistration(registration: String): IO[String, Aircraft] =
        ZIO.succeed(Aircraft(registration))

      override def countByAirline(code: String): IO[String, Int] =
        ZIO.succeed(20)

      override def count(): IO[String, Int] = for {
        _ <- database.doQuery("dummy query")
      } yield 100
    }

    object AircraftRepositoryLive {
      val layer: URLayer[Database, AircraftRepository] =
        ZLayer.fromFunction(db => AircraftRepositoryLive(db))
    }

  }

  object Services {

    trait AuditService {
      def countAircraftByAirline(airlineCode: String): IO[String, Int]

      def countAll(): IO[String, AuditCounters]
    }

    case class AuditServiceLive(
        airlineRepository: AirlineRepository,
        aircraftRepository: AircraftRepository
    ) extends AuditService {

      override def countAircraftByAirline(airlineCode: String): IO[String, Int] =
        aircraftRepository.countByAirline(airlineCode)

      override def countAll(): IO[String, AuditCounters] = for {
        airlines  <- airlineRepository.count()
        aircrafts <- aircraftRepository.count()
      } yield AuditCounters(airlines, aircrafts)

    }

    object AuditServiceLive {
      case class AuditServiceDeps(airlineRepository: AirlineRepository, aircraftRepository: AircraftRepository)

      val layer: URLayer[AirlineRepository with AircraftRepository, AuditService] =
        ZLayer {
          for {
            airlineRepo  <- ZIO.service[AirlineRepository]
            aircraftRepo <- ZIO.service[AircraftRepository]
          } yield AuditServiceLive(airlineRepo, aircraftRepo)
        }

    }

  }

  object Program {
    val auditProgram = for {
      auditService <- ZIO.service[AuditService]
      counters     <- auditService.countAll()
    } yield counters
  }

  type ApplicationDependencies =
    Config with Database with AirlineRepository with AircraftRepository with AuditService

  override def run = {

    val applicationLayer =
      ZLayer.make[ApplicationDependencies](
        ConfigLive.layer,
        DatabaseLive.layer,
        AirlineRepositoryLive.layer,
        AircraftRepositoryLive.layer,
        AuditServiceLive.layer,
        ZLayer.Debug.mermaid
      )

    val program = for {
      // args         <- getArgs
      _            <- ZIO.log("start application")
      config       <- ZIO.service[Config]
      appCfg       <- config.read("filepath")
      _            <- ZIO.log(s"log level: ${appCfg.logLevel}")
      auditService <- ZIO.service[AuditService]
      counters     <- auditService.countAll()
      _            <- ZIO.log(s"counters: $counters")
      _            <- ZIO.log("stop application")
    } yield ()

    program.provide(applicationLayer)

  }

}
