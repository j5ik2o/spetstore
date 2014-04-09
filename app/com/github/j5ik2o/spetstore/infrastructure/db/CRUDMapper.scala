package com.github.j5ik2o.spetstore.infrastructure.db

import skinny.orm.SkinnyCRUDMapper
import skinny.orm.feature.OptimisticLockWithVersionFeatureWithId

trait CRUDMapper[T] extends SkinnyCRUDMapper[T] /**with OptimisticLockWithVersionFeatureWithId[Long, T]*/ {

  override def primaryKeyFieldName = "pk"

  /*override val lockVersionFieldName = "version"*/

  def toNamedValues(record: T): Seq[(Symbol, Any)]

}
