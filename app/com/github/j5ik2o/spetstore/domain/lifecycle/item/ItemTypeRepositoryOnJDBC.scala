package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import java.util.UUID

import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.domain.model.item.{CategoryId, ItemType, ItemTypeId}


private[item]
class ItemTypeRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemTypeId, ItemType] with ItemTypeRepository {

  class Dao extends AbstractDao[ItemType] {

    override def defaultAlias = createAlias("i")

    override def tableName: String = "item_type"

    def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[ItemType]): ItemType =
      ItemType(
        id = ItemTypeId(UUID.fromString(rs.get(n.id))),
        categoryId = CategoryId(UUID.fromString(rs.get(n.categoryId))),
        name = rs.get(n.name),
        description = rs.get(n.description)
      )

    def toNamedValues(entity: ItemType): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id.value,
      'categoryId -> entity.categoryId.value,
      'name -> entity.name,
      'description -> entity.description
    )

  }

  override protected def createDao = new Dao

}
