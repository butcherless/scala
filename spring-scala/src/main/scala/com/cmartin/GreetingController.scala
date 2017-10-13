package com.cmartin

import java.time.{LocalDate, LocalDateTime}

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam}
import org.slf4j.{Logger, LoggerFactory}

@Controller
class GreetingController {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  @RequestMapping(Array("/greeting"))
  def greeting(@RequestParam(value = "name", required = false, defaultValue = "donald")
      name: String, model: Model): String = {
    logger.debug(s"name: ${name}")
    //model.addAttribute("name", name)
    val person = new Person(1, name, "lastName", s"${name}@duck.com")
    model.addAttribute("name", person.firstName)
    model.addAttribute("email", person.email)
    model.addAttribute("date", LocalDateTime.now())

    "greeting"
  }
}
