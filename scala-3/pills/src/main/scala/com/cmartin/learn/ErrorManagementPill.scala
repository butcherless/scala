package com.cmartin.learn

import zio.{IO, ZIO}

object ErrorManagementPill {

  object DomainLayer {
    case class Task(name: String, definition: String)
  }

  object AdapterLayer {
    import DomainLayer._
    import ServiceLayer._
    import AdapterValidator._
    import ServiceLayer.CreateRequest

    enum AdapterError {
      case BadRequest(message: String)
      case NotFound(message: String)
      case Conflict(message: String)
      case ServerError(message: String)
    }

    trait TaskApi {
      def doPost(postReq: Map[String, String]): IO[AdapterError, String]
    }

    class TaskApiImpl(service: ServiceLayer.TaskService) extends TaskApi {
      override def doPost(
          postReq: Map[String, String]
      ): IO[AdapterError, String] = {
        val program: IO[Any, String] = for {
          createRequest <- AdapterValidator.validatePost(postReq)
          task <- service.create(createRequest)
        } yield task.toString

        program.mapError {
          case ValidationError.MissingName(m) =>
            AdapterError.BadRequest(m)
          case ServiceError.CreateError(m) =>
            AdapterError.Conflict(m)
          case _ => AdapterError.ServerError("unexpected error")
        }
      }
    }

    object TaskApiImpl {
      def apply(service: ServiceLayer.TaskService): TaskApiImpl =
        new TaskApiImpl(service)
    }

    object AdapterValidator {

      import AdapterError._

      enum ValidationError {
        case MissingName(message: String)
        case InvalidOwner(message: String)
        case InvalidDefinition(message: String)
      }

      def validatePost(
          request: Map[String, String]
      ): IO[ValidationError, CreateRequest] = {
        val name = request.getOrElse("name", "")
        if (name.nonEmpty) {
          IO.succeed(
            CreateRequest(
              request("owner"),
              Task(request("name"), request("def"))
            )
          )
        } else {
          IO.fail(ValidationError.MissingName("missing name"))
        }
      }
    }
  }

  object ServiceLayer {
    import DomainLayer._
    case class CreateRequest(owner: String, task: Task)

    enum ServiceError {
      case CreateError(message: String)
      case UpdateError(message: String)
      case DeleteError(message: String)
      case UnexpectedError(message: String)
    }

    trait TaskService {
      def create(createReq: CreateRequest): IO[ServiceError, Task]
    }

    // TODO repository dependency
    class TaskServiceImpl extends TaskService {
      override def create(createReq: CreateRequest): IO[ServiceError, Task] =
        createReq.task.definition match {
          case "error-duplicate" =>
            IO.fail(ServiceError.CreateError("duplicate-task"))
          case _ => IO.succeed(createReq.task)
        }
    }

    object TaskServiceImpl {
      def apply(): TaskServiceImpl =
        new TaskServiceImpl()
    }

  }

  object RepositoryLayer {
    enum RepositoryError:
      case MissingEntity(message: String, throwable: Option[Throwable])
      case DuplicateEntity(message: String, throwable: Option[Throwable])
      case UnexpectedError(message: String, throwable: Option[Throwable])
  }

  object DatabaseLayer {
    trait DatabaseService {
      def insert[A](a: A): Long
    }

    // TODO
    class DatabaseServiceImpl extends DatabaseService {
      override def insert[A](a: A): Long = a.hashCode.toLong
    }
  }

}
