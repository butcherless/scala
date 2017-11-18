package com.cmartin

import java.time.{LocalDateTime, ZonedDateTime}

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam}

@Controller
class GreetingController {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  @Autowired private val properties: ApplicationProperties = null
  @Autowired private val service: GreetingService = null


  @RequestMapping(Array("/random"))
  def random(@RequestParam(value = "number", required = false, defaultValue = "0") number: Int,
             model: Model): String = {
    model.addAttribute("integer", service.generateRandom(5))
    model.addAttribute("date", ZonedDateTime.now())

    "random"
  }

  @RequestMapping(Array("/greeting"))
  def greeting(@RequestParam(value = "name", required = false, defaultValue = "donald") name: String,
               model: Model): String = {
    logger.debug(s"name: ${name}")
    val person = new Person(1, name, "lastName", s"${name.toLowerCase()}@duck.com")
    model.addAttribute("name", person.firstName)
    model.addAttribute("email", person.email)
    model.addAttribute("date", LocalDateTime.now())

    "greeting"
  }

  @RequestMapping(Array("/"))
  def home(model: Model): String = {
    model.addAttribute("version", properties.version)
    model.addAttribute("date", ZonedDateTime.now())

    "home"
  }
}
