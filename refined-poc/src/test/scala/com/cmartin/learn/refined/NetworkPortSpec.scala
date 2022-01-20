package com.cmartin.learn.refined

class NetworkPortSpec extends PropertySpec {

  val wellKnownPorts = Table("port", 0, 1, 22, 80, 1023)
  val userPorts      = Table("port", 1024, 8080, 9001, 65535)
  val invalidPorts   = Table("port", -1, 65536, 70000)

  property(
    "wellKnownPorts should contains valid well known network port values"
  ) {
    forAll(wellKnownPorts) { port =>
      validateWellKnownPort(port) contains port
    }
  }

  property("userPorts should contains valid user network port values") {
    forAll(userPorts) { port =>
      validateUserPort(port) contains port
    }
  }

  property(
    "wellKnownPorts and userPorts should contains valid user network port values"
  ) {
    forAll(wellKnownPorts) { port =>
      validateNetworkPort(port) contains port
    }
    forAll(userPorts) { port =>
      validateNetworkPort(port) contains port
    }
  }

  property("invalidPorts should contains invalid network port values") {
    forAll(invalidPorts) { port =>
      validateUserPort(port).isLeft shouldBe true
    }
  }
}
