package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[item]
class ItemRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemId, Item] with ItemRepository {

  override def tableName: String = "item"

  override def columnNames: Seq[String] = Seq(
    "id",
    "item_type_id",
    "name",
    "description",
    "price",
    "quantity"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Item =
    Item(
      id = ItemId(UUID.fromString(resultSet.string("id"))),
      itemTypeId = ItemTypeId(UUID.fromString(resultSet.string("item_type_id"))),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description"),
      price = resultSet.long("price"),
      quantity = resultSet.int("quantity")
    )

  protected def convertEntityToValues(entity: Item): Seq[Any] = Seq(
    entity.id,
    entity.itemTypeId.value.toString,
    entity.name,
    entity.description,
    entity.price,
    entity.quantity
  )
}
