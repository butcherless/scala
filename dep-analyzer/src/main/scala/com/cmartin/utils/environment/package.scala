package com.cmartin.utils

import com.cmartin.utils.Domain.Gav
import zio.ZIO

package object environment {

  def getLinesFromFile(filename: String): ZIO[FileManagerEnv, Throwable, List[String]] =
    ZIO.accessM(_.fileManagerEnv getLinesFromFile filename)

//  def parseDepLine(line: String): ZIO[FileManagerEnv, String,  Gav] =
//    ZIO.accessM(_.fileManagerEnv parseDepLine line)
}
