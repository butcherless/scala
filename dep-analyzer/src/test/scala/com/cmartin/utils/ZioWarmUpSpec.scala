//package com.cmartin.utils
//
//import com.cmartin.utils.Domain.FileIOError
//import org.scalatest.{FlatSpec, Matchers}
//import zio.{Task, _}
//
//class ZioWarmUpSpec extends FlatSpec with Matchers with DefaultRuntime {
//
//  "ZIO UIO effect" should "return a value" in {
//    // given
//    val a: Int = 2
//    val b: Int = 2
//
//    // when
//    val program: UIO[Int] = sum(a, b)
//
//    val result: Int = unsafeRun(program)
//
//    // then
//    assert(result == 4)
//  }
//
//  "ZIO Task effect" should "return a valid Json" in {
//    // given
//    val message = "{...non empty message simulation...}"
//    //    val message = ""
//
//    // when
//    val effectResult: UIO[Either[Throwable, Json]] = simulateParseJson(message)
//
//    // then
//    val jsonEither: Either[Throwable, Json] = unsafeRun(effectResult)
//
//    jsonEither.contains(Json(jsonKeys)) shouldBe true
//  }
//
//  it should "return a runtime exception" in {
//    // given
//    val message = ""
//
//    // when
//    val effectResult: UIO[Either[Throwable, Json]] = simulateParseJson(message)
//
//    // then
//    val jsonEither = unsafeRun(effectResult)
//
//    //val exception = jsonEither.fold(e => e, v => v)
//    val isRuntimeException = jsonEither.swap.exists(_.isInstanceOf[RuntimeException])
//
//    isRuntimeException shouldBe true
//  }
//
//  it should "extract an action from the json message" in {
//    val message = "{...valid action simulation...}"
//
//    val effectResult: UIO[Either[JsonError, Action]] = simulateJsonMapping(message)
//
//    val action: Either[JsonError, Action] = unsafeRun(effectResult)
//
//    action.contains(Action(message, 0)) shouldBe true
//  }
//
//  it should "fail trying to extract an action from the json message" in {
//    val message = ""
//
//    val effectResult: UIO[Either[JsonError, Action]] = simulateJsonMapping(message)
//
//    val error: Either[JsonError, Action] = unsafeRun(effectResult)
//
//    error.swap.contains(MappingError("invalid type")) shouldBe true
//  }
//
//  it should "process actions in parallel" in {
//    import ZioWarmUpSpec._
//
//    val time0 = System.currentTimeMillis()
//
//    val dependencies: Task[List[Gav]] = ZIO.foreachParN(5)(artifactList)(checkDependency)
//    val result: Seq[Gav]              = unsafeRun(dependencies)
//
//    val time1       = System.currentTimeMillis()
//    val timeElapsed = (time1 - time0) / 1000.toDouble
//    println(s"processing time was: $timeElapsed, result: ${result.mkString("\n[\n", "\n", "\n]")}")
//  }
//
//  def doBusinessOperation(message: String): IO[JsonError, String] = {
//    val effect = IO
//      .effect {
//        message match {
//          case ""        => throw ParsingException("invalid format")
//          case "mapping" => throw MappingException("invalid type")
//          case "unknown" => throw new RuntimeException("business exception")
//          case _         => "{...processed message...}"
//        }
//      }
//      .mapError {
//        case ParsingException(m) => ParsingError(m)
//        case MappingException(m) => MappingError(m)
//        case _                   => UnknownError("unknown error")
//      }
//
//    effect
//  }
//
//  // work with IO and Throwable
//  it should "return processed message" in {
//
//    val prog: IO[JsonError, String] = for {
//      _ <- doBusinessOperation("1")
//      r <- doBusinessOperation("2")
//    } yield r
//
//    val result: String = unsafeRun(
//      prog
//        .catchAll(_ => IO.succeed("error"))
//    )
//
//    result shouldBe "{...processed message...}"
//  }
//
//  it should "return parsing error" in {
//
//    val prog: IO[JsonError, String] = for {
//      _ <- doBusinessOperation("")
//      r <- doBusinessOperation("2")
//    } yield r
//
//    val result: Either[JsonError, String] = unsafeRun(
//      prog.either
//    )
//    result.swap.contains(ParsingError("invalid format")) shouldBe true
//  }
//
//  it should "return mapping error" in {
//
//    val prog: IO[JsonError, String] = for {
//      _ <- doBusinessOperation("1")
//      r <- doBusinessOperation("mapping")
//    } yield r
//
//    val result: Either[JsonError, String] = unsafeRun(
//      prog.either
//    )
//    result.swap.contains(MappingError("invalid type")) shouldBe true
//  }
//
//  it should "return unknown error" in {
//
//    val prog: IO[JsonError, String] = for {
//      _ <- doBusinessOperation("1")
//      r <- doBusinessOperation("unknown")
//    } yield r
//
//    val result: Either[JsonError, String] = unsafeRun(
//      prog.either
//    )
//    result.swap.contains(UnknownError("unknown error")) shouldBe true
//  }
//
//  // Liskov substitution principle soLid
//  "Sealed trait pattern matching" should "match a subclass" in {
//    val error: JsonError = UnknownError("unknown error")
//
//    val result = error match {
//      case e: JsonError => "subclass"
//      case _            => "not a subclass"
//    }
//
//    assert(result == "subclass")
//  }
//
//  "FileManager" should "read all the lines from a file" in {
//
//    val filename = "dep-analyzer/src/main/resources/deps.log"
//
//    val program = OldFileManager.getLinesFromFile(filename).refineOrDie {
//      case e: java.io.IOException => FileIOError(s"IO access error: ${e.getMessage}")
//    }
//    val lines = unsafeRun(program.either)
//
//    lines.isRight shouldBe true
//    lines.map { i =>
//      assert(i.nonEmpty)
//    }
//  }
//
//  "FileManager" should "return an error trying to read all the lines from a file" in {
//    val errorMessage = "IO access error"
//
//    val filename = "non-existent.file"
//
//    val program = OldFileManager.getLinesFromFile(filename).refineOrDie {
//      case e: java.io.IOException => FileIOError(s"$errorMessage: ${e.getMessage}")
//    }
//    val lines = unsafeRun(program.either)
//
//    lines.isLeft shouldBe true
//    lines.swap.map { e =>
//      assert(e.isInstanceOf[FileIOError])
//    }
//  }
//
//  //TODO tests ZIO.sleep(duration)
//  it should "sleep the fiber" in {
//    import zio.duration._
//    val program = for {
//      _ <- UIO.succeed(println(1))
//      _ <- ZIO.sleep(2.second)
//      _ <- UIO.succeed(println(2))
//    } yield ()
//
//    unsafeRun(program)
//  }
//
//}
//
//object ZioWarmUpSpec {
//  val artifactList: List[Gav] = List(
//    Gav("ch.qos.logback", "logback-classic", "1.2.3"),
//    Gav("ch.qos.logback", "logback-core", "1.2.3"),
//    Gav("com.chuusai", "shapeless_2.13", "2.3.3"),
//    Gav("com.cmartin.learn", "depanalyzer_2.13", "1.0.0-SNAPSHOT"),
//    Gav("com.softwaremill.sttp", "akka-http-backend_2.13", "1.6.8"),
//    Gav("com.softwaremill.sttp", "core_2.13", "1.6.8"),
//    Gav("com.softwaremill.sttp", "json-common_2.13", "1.6.8"),
//    Gav("com.softwaremill.sttp", "json4s_2.13", "1.6.8"),
//    Gav("com.thoughtworks.paranamer", "paranamer", "2.8"),
//    Gav("com.typesafe", "config", "1.3.3"),
//    Gav("com.typesafe", "ssl-config-core_2.13", "0.3.8"),
//    Gav("com.typesafe.akka", "akka-actor_2.13", "2.5.25"),
//    Gav("com.typesafe.akka", "akka-http-core_2.13", "10.1.9"),
//    Gav("com.typesafe.akka", "akka-http_2.13", "10.1.9"),
//    Gav("com.typesafe.akka", "akka-parsing_2.13", "10.1.9"),
//    Gav("com.typesafe.akka", "akka-protobuf_2.13", "2.5.25"),
//    Gav("com.typesafe.akka", "akka-stream_2.13", "2.5.25"),
//    Gav("dev.zio", "zio-stacktracer_2.13", "1.0.0-RC13"),
//    Gav("dev.zio", "zio_2.13", "1.0.0-RC13"),
//    Gav("io.circe", "circe-core_2.13", "0.12.1"),
//    Gav("io.circe", "circe-generic_2.13", "0.12.1"),
//    Gav("io.circe", "circe-jawn_2.13", "0.12.1"),
//    Gav("io.circe", "circe-numbers_2.13", "0.12.1"),
//    Gav("io.circe", "circe-parser_2.13", "0.12.1"),
//    Gav("org.json4s", "json4s-ast_2.13", "3.6.7"),
//    Gav("org.json4s", "json4s-core_2.13", "3.6.7"),
//    Gav("org.json4s", "json4s-native_2.13", "3.6.7"),
//    Gav("org.json4s", "json4s-scalap_2.13", "3.6.7"),
//    Gav("org.reactivestreams", "reactive-streams", "1.0.2"),
//    Gav("org.scala-lang.modules", "scala-java8-compat_2.13", "0.9.0"),
//    Gav("org.scala-lang.modules", "scala-parser-combinators_2.13", "1.1.2"),
//    Gav("org.slf4j", "slf4j-api", "1.7.25"),
//    Gav("org.typelevel", "cats-core_2.13", "2.0.0"),
//    Gav("org.typelevel", "cats-kernel_2.13", "2.0.0"),
//    Gav("org.typelevel", "cats-macros_2.13", "2.0.0"),
//    Gav("org.typelevel", "jawn-parser_2.13", "0.14.2")
//  )
//
//}
