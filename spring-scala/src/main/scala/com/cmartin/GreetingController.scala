package com.cmartin

import java.time.ZonedDateTime
import java.util.UUID

import com.cmartin.algebra.GreetingService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, MediaType, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.util.{Failure, Success}

@RestController
class GreetingController {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  @Autowired private val properties: ApplicationProperties = null
  @Autowired val service: GreetingService = null

  @GetMapping(path = Array("/random/{number}"), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  def random(@PathVariable number: Int): ResponseEntity[SourceTargetPair] = {
    //("date", ZonedDateTime.now())
    logger.debug(s"input: ${number}")
    // TODO resolve Try/Success/Failure
    val stp = SourceTargetPair(number.toString,
      service.generateRandom(number, properties.maxRandom).get.toString,
      properties.maxRandom)

    logger.debug(s"entity: ${stp}")

    new ResponseEntity[SourceTargetPair](stp, HttpStatus.OK)
  }

  @GetMapping(path = Array("/greeting"), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  def greeting(@RequestParam(value = "name", required = false, defaultValue = "Unknown")
               name: String): ResponseEntity[Person] = {
    logger.debug(s"input: ${name}")
    val person = Person(1, name, name + "-lastName", s"${name.toLowerCase()}@duck.com")
    logger.debug(s"output: ${person}")

    new ResponseEntity[Person](person, HttpStatus.OK)
  }

  @GetMapping(path = Array("/"), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  def home(): ResponseEntity[ApplicationVersion] = {
    val appVersion = ApplicationVersion(properties.version, ZonedDateTime.now().toString)

    logger.debug(s"version: ${appVersion}")

    new ResponseEntity[ApplicationVersion](appVersion, HttpStatus.OK)
  }

  //TODO 404 json error message
  @GetMapping(path = Array("/randomWord/{number}"), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  def randomTarget(@PathVariable number: Int): ResponseEntity[_] = {
    logger.debug(s"input: ${number}")
    val stPair = service.generateRandomPair(number, properties.maxRandom)

    logger.debug(s"output: ${stPair}")
    stPair match {
      case Success(dto) => new ResponseEntity[SourceTargetPair](dto, HttpStatus.OK)
      case Failure(ex) => new ResponseEntity[String](ex.getMessage, HttpStatus.NOT_FOUND)
    }
  }

  @DeleteMapping(path = Array("/delete/{id}"))
  def deletePerson(@PathVariable id: String): ResponseEntity[String] = {
    logger.debug(s"delete entity: ${id}")

    new ResponseEntity[String](getRandomId, HttpStatus.OK)
  }


  @PostMapping(path = Array("/create"), consumes = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  @ResponseStatus(HttpStatus.CREATED)
  def createPerson(@RequestBody person: Person): ResponseEntity[String] = {
    logger.debug(s"entity: {} ", person)

    new ResponseEntity[String](getRandomId, HttpStatus.CREATED)
  }

  private def getRandomId = UUID.randomUUID().toString

}
