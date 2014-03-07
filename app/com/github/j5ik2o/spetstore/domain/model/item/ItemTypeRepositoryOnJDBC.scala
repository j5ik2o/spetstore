package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.{SQLInterpolation, WrappedResultSet}
import java.util.UUID

import scalikejdbc._, SQLInterpolation._


private[item]
class ItemTypeRepositoryOnJDBC
extends RepositoryOnJDBC[ItemTypeId, ItemType] with ItemTypeRepository {

  override def defaultAlias = createAlias("i")

  override def tableName: String = "item_type"

  def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[ItemType]): ItemType =
    ItemType(
      id = ItemTypeId(UUID.fromString(rs.get(n.id))),
      categoryId = CategoryId(UUID.fromString(rs.get(n.categoryId))),
      name = rs.get(n.name),
      description = rs.get(n.description)
    )

  protected def toNamedValues(entity: ItemType): Seq[(Symbol, Any)] = Seq(
    'id -> entity.id.value,
    'categoryId -> entity.categoryId.value,
    'name -> entity.name,
    'description -> entity.description
  )

}
