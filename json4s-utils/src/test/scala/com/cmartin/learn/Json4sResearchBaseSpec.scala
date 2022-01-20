package com.cmartin.learn

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class Json4sResearchBaseSpec extends AnyFlatSpec with Matchers {
  protected val shadowRepository: ShadowRepository = ShadowDummyRepository()
  protected val shadowService: ShadowService       = ShadowService(shadowRepository)

}
