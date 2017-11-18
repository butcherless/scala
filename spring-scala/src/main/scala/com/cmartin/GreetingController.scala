package com.cmartin

import java.time.{LocalDateTime, ZonedDateTime}

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestParam}

@Controller
class GreetingController {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  @Autowired private val properties: ApplicationProperties = null
  @Autowired private val service: GreetingService = null


  @GetMapping(path = Array("/random/{number}"), produces = Array(MediaType.TEXT_HTML_VALUE))
  def random(@PathVariable number: Int,
             model: Model): String = {
    model.addAttribute("integer", service.generateRandom(number, properties.maxRandom))
    model.addAttribute("date", ZonedDateTime.now())

    "random"
  }

  @GetMapping(path = Array("/greeting"), produces = Array(MediaType.TEXT_HTML_VALUE))
  def greeting(@RequestParam(value = "name", required = false, defaultValue = "donald") name: String,
               model: Model): String = {
    logger.debug(s"name: ${name}")
    val person = new Person(1, name, "lastName", s"${name.toLowerCase()}@duck.com")
    model.addAttribute("name", person.firstName)
    model.addAttribute("email", person.email)
    model.addAttribute("date", LocalDateTime.now())

    "greeting"
  }

  @GetMapping(path = Array("/"), produces = Array(MediaType.TEXT_HTML_VALUE))
  def home(model: Model): String = {
    model.addAttribute("version", properties.version)
    model.addAttribute("date", ZonedDateTime.now())

    "home"
  }
}
