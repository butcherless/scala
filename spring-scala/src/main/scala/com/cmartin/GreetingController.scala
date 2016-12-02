package com.cmartin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam}
import org.slf4j.{Logger, LoggerFactory}

@Controller
class GreetingController {
  val logger = LoggerFactory.getLogger(this.getClass)


  @RequestMapping(Array("/greeting"))
  def greeting(@RequestParam(value = "name", required = false, defaultValue = "World") name: String, model: Model): String = {
    logger.debug(s"name: ${name}")
    //model.addAttribute("name", name)
    val person = new Person(1, name, "lastName", s"${name}@domain.com")
    model.addAttribute("name", person.getFirstName)
    model.addAttribute("email", person.getEmail)
    "greeting"
  }
}
