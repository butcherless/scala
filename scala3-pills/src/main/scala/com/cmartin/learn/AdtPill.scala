package com.cmartin.learn

object AdtPill {
  enum ResponseError:
    case BadRequest(message: String)
    case NotFound(message: String)
    case Conflict(message: String)
}
