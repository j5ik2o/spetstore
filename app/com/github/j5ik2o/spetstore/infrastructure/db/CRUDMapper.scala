package com.github.j5ik2o.spetstore.infrastructure.db

import skinny.orm.SkinnyCRUDMapper

trait CRUDMapper[T] extends SkinnyCRUDMapper[T] {

//  override def useAutoIncrementPrimaryKey = false

  override def primaryKeyFieldName = "pk"

  def toNamedValues(record: T): Seq[(Symbol, Any)]

}
