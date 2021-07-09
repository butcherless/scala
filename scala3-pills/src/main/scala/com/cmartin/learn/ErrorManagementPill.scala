package com.cmartin.learn

import zio.IO

object ErrorManagementPill {

  object AdapterLayer {
    enum AdapterError {
      case BadRequest(message: String)
      case NotFound(message: String)
      case Conflict(message: String)
      case ServerError(message:String)
    }

    trait TaskApi {
      def doPost(postReq: String): IO[AdapterError, String]
    }

    class TaskApiImpl(service: ServiceLayer.TaskService) extends TaskApi {
      override def doPost(postReq: String): IO[AdapterError, String] = {
        service.create(postReq)
          .mapError {
            case _ => AdapterError.Conflict("")
          }
      }
    }

    object TaskApiImpl {
      def apply(service: ServiceLayer.TaskService): TaskApiImpl =
        new TaskApiImpl(service)
    }
  }

  object ServiceLayer {
    enum ServiceError {
      case CreateError(message: String)
      case UpdateError(message: String)
      case DeleteError(message: String)
      case UnexpectedError(message: String)
    }

    trait TaskService {
      def create(createReq: String): IO[ServiceError, String]
    }

    //TODO repository dependency
    class TaskServiceImpl extends TaskService {
      override def create(createReq: String): IO[ServiceError, String] = createReq match {
        case "create-error" => IO.fail(ServiceError.CreateError("unable to create the task"))
        case _ => IO.succeed("1")
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

    //TODO
    class DatabaseServiceImpl extends DatabaseService {
      override def insert[A](a: A): Long = a.hashCode.toLong
    }
  }
  
}
