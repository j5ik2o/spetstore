package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.{SQLInterpolation, WrappedResultSet}
import java.util.UUID
import com.github.j5ik2o.spetstore.domain.model.basic.SexType

private[item]
class ItemRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemId, Item] with ItemRepository {

  override def tableName: String = "item"

  override def columnNames: Seq[String] = Seq(
    "id",
    "item_type_id",
    "sex_type",
    "name",
    "description",
    "price",
    "supplier_id"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Item =
    Item(
      id = ItemId(UUID.fromString(resultSet.string("id"))),
      itemTypeId = ItemTypeId(UUID.fromString(resultSet.string("item_type_id"))),
      sexType = SexType(resultSet.int("sex_type")),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description"),
      price = resultSet.long("price"),
      supplierId = SupplierId(UUID.fromString(resultSet.string("supplier_id")))
    )

  protected def convertEntityToValues(entity: Item): Seq[Any] = Seq(
    entity.id,
    entity.itemTypeId.value.toString,
    entity.sexType.id,
    entity.name,
    entity.description,
    entity.price,
    entity.supplierId.value.toString
  )

  protected def toNamedValues(entity: Item): Seq[(Symbol, Any)] = ???

  def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Item]): Item = ???
}
