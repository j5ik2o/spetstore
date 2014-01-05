package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[item]
class ItemTypeRepositoryOnJDBC
extends RepositoryOnJDBC[ItemTypeId, ItemType] with ItemTypeRepository {

  override def tableName: String = "item_type"

  override def columnNames: Seq[String] = Seq(
    "id",
    "category_id",
    "name",
    "description"
  )

  protected def convertToEntity(resultSet: WrappedResultSet): ItemType =
    ItemType(
      id = ItemTypeId(UUID.fromString(resultSet.string("id"))),
      categoryId = CategoryId(UUID.fromString("category_id")),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description")
    )

  protected def convertToValues(entity: ItemType): Seq[Any] = Seq(
    entity.id,
    entity.categoryId.value.toString,
    entity.name,
    entity.description
  )

}
