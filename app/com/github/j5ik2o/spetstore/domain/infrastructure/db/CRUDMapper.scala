package com.github.j5ik2o.spetstore.domain.infrastructure.db

import skinny.orm.SkinnyCRUDMapper

trait CRUDMapper[T] extends SkinnyCRUDMapper[T] {
  def toNamedValues(record: T): Seq[(Symbol, Any)]
}
