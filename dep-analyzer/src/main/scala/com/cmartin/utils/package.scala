package com.cmartin

import com.cmartin.utils.domain.LogicManager
import com.colofabrix.scala.figlet4s.options.HorizontalLayout
import com.colofabrix.scala.figlet4s.unsafe.{FIGureOps, Figlet4s, OptionsBuilderOps}
import zio.{ExitCode, UIO, ZIO, ZIOAspect}
import zio.{Clock, URIO}

import java.util.concurrent.TimeUnit

package object utils {
  val toExitCode: Int => ExitCode =
    ExitCode.apply

  def getMillis(): URIO[Clock, Long] =
    Clock.currentTime(TimeUnit.MILLISECONDS)

  def calcElapsedMillis(start: Long): URIO[Clock, Long] =
    Clock.currentTime(TimeUnit.MILLISECONDS).map(_ - start)

  def printBanner(message: String): UIO[Unit] =
    ZIO.succeed(
      Figlet4s.builder()
        .withInternalFont("doom")
        .withMaxWidth(120)
        .withHorizontalLayout(HorizontalLayout.HorizontalFitting)
        .render(message)
        .print()
    )

  // loggers
  def genericLog[T](message: String) =
    ZIOAspect.loggedWith[T](a => s"$message: $a")

  def iterableLog(message: String) =
    ZIOAspect.loggedWith[Iterable[_]](i => s"$message:${i.mkString("\n", "\n", "")}")

  def iterablePairLog(message: String) =
    ZIOAspect.loggedWith[LogicManager.ParsedLines] { parsedLines =>
      parsedLines.failedList match {
        case Nil => s"$message: empty sequence of elements"
        case it  => s"$message:${it.mkString("\n", "\n", "")}"
      }
    }

}
