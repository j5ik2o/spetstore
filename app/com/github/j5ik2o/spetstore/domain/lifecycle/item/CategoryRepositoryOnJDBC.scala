package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.SimpleRepositoryOnJDBC
import com.github.j5ik2o.spetstore.domain.model.item.{Category, CategoryId}
import com.github.j5ik2o.spetstore.infrastructure.db.CategoryRecord
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType


private[item]
class CategoryRepositoryOnJDBC
  extends SimpleRepositoryOnJDBC[CategoryId, Category] with CategoryRepository {

  override type T = CategoryRecord

  override protected val mapper = CategoryRecord

  override protected def convertToEntity(record: TS): Category = Category(
    id = CategoryId(record.id),
    status = StatusType(record.status),
    name = record.name,
    description = record.description
  )

  override protected def convertToRecord(entity: Category): TS = CategoryRecord(
    id = entity.id.value,
    status = entity.status.id,
    name = entity.name,
    description = entity.description
  )

}
