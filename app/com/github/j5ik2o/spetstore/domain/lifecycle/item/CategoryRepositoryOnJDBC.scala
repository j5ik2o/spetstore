package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.SimpleRepositoryOnJDBC
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.{ Category, CategoryId }
import com.github.j5ik2o.spetstore.infrastructure.db.CategoryRecord

private[item] class CategoryRepositoryOnJDBC
    extends SimpleRepositoryOnJDBC[CategoryId, Category] with CategoryRepository {

  override type T = CategoryRecord

  override protected lazy val mapper = CategoryRecord

  override protected def convertToEntity(record: T): Category = Category(
    id = CategoryId(record.id),
    status = StatusType(record.status),
    name = record.name,
    description = record.description,
    version = Some(record.version)
  )

  override protected def convertToRecord(entity: Category) = CategoryRecord(
    id = entity.id.value,
    status = entity.status.id,
    name = entity.name,
    description = entity.description,
    version = entity.version.getOrElse(1)
  )

}
