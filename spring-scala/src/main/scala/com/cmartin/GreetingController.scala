package com.cmartin

import java.time.{LocalDateTime, ZonedDateTime}

import com.cmartin.algebra.GreetingService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, MediaType, ResponseEntity}
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._

@RestController
class GreetingController {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  @Autowired private val properties: ApplicationProperties = null
  @Autowired val service: GreetingService = null

  @GetMapping(path = Array("/random/{number}"), produces = Array(MediaType.TEXT_HTML_VALUE))
  def random(@PathVariable number: Int,
             model: Model): String = {
    model.addAttribute("source", number)
    model.addAttribute("target", service.generateRandom(number, properties.maxRandom))
    model.addAttribute("date", ZonedDateTime.now())

    "random"
  }

  @GetMapping(path = Array("/greeting"), produces = Array(MediaType.TEXT_HTML_VALUE))
  def greeting(@RequestParam(value = "name", required = false, defaultValue = "donald") name: String,
               model: Model): String = {
    logger.debug(s"name: ${name}")
    val person = newPerson(1, name, "lastName", s"${name.toLowerCase()}@duck.com")
//    model.addAttribute("name", person.firstName)
//    model.addAttribute("email", person.email)
    model.addAttribute("date", LocalDateTime.now())

    "greeting"
  }

  @GetMapping(path = Array("/"), produces = Array(MediaType.TEXT_HTML_VALUE))
  def home(model: Model): String = {
    model.addAttribute("version", properties.version)
    model.addAttribute("date", ZonedDateTime.now())

    "home"
  }

  @GetMapping(path = Array("/randomWord/{number}"), produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  def randomTarget(@PathVariable number: Int,
                   model: Model): String = {
    val stPair = service.generateRandomPair(number, properties.maxRandom)
    model.addAttribute("source", stPair.source)
    model.addAttribute("target", stPair.target)
    model.addAttribute("limit", stPair.limit)
    model.addAttribute("date", ZonedDateTime.now())

    "randomWord"
  }

  @DeleteMapping(path = Array("/delete/{number}"))
  def deletePerson(@PathVariable number: Int): String = {
    logger.debug(s"delete entity: ${number}")

    "delete"
  }


  @PostMapping(path = Array("/create"), consumes = Array(MediaType.APPLICATION_JSON_UTF8_VALUE))
  @ResponseStatus(HttpStatus.CREATED)
  def createPerson(@RequestBody person: Person): ResponseEntity[Long] = {
    logger.debug(s"entity: {} ", person)

    new ResponseEntity[Long](1, HttpStatus.CREATED)
  }

  //TODO
  private def newPerson(id: Int, firstName: String, lastName: String, email: String) = {
    val p = new Person(1)
//    p.id = id
//    p.firstName = firstName
//    p.lastName = lastName
//    p.email = email

    p
  }

}
