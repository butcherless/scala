package com.cmartin.learn.refined

class NetworkPortSpec extends PropertySpec {

  val wellKnownPorts = Table("port", 0, 1, 22, 80, 1023)
  val userPorts = Table("port", 1024, 8080, 9001, 65535)
  val invalidPorts = Table("port", -1, 65536, 70000)

  property("wellKnownPorts should contains valid well known network port values") {
    forAll(wellKnownPorts) {
      port => validateWellKnownPort(port).isRight shouldBe true
    }
  }

  property("userPorts should contains valid user network port values") {
    forAll(userPorts) {
      port => validateUserPort(port).isRight shouldBe true
    }
  }

  property("wellKnownPorts and userPorts should contains valid user network port values") {
    forAll(wellKnownPorts) {
      port => validateNetworkPort(port).isRight shouldBe true
    }
    forAll(userPorts) {
      port => validateNetworkPort(port).isRight shouldBe true
    }
  }

  property("invalidPorts should contains invalid network port values") {
    forAll(invalidPorts) {
      port => validateUserPort(port).isLeft shouldBe true
    }
  }
}
