package com.cmartin.utils.file

import com.cmartin.utils.Domain.Gav
import com.cmartin.utils.http.{HttpManager, HttpManagerLive}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.{default => runtime}

class HttpManagerITSpec
    extends AnyFlatSpec with Matchers {

  behavior of "HttpManager"

  it should "retrieve a single dependency change" in {
    // given
    val g = "dev.zio"
    val a = "zio_2.13"
    val v = "1.0.0"
    val gav = Gav(g, a, v)
    val deps = Seq(gav)
    // when
    val program = HttpManager(_.checkDependencies(deps))

    val (errors, remoteDeps) = runtime.unsafeRun(
      program.provide(HttpManagerLive.layer)
    )

    info(s"errors: $errors")
    info(s"remoteDeps: $remoteDeps")
    // then
  }

}
