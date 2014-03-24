package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import com.github.j5ik2o.spetstore.domain.model.item.{SupplierId, ItemTypeId, Item, ItemId}
import java.util.UUID
import scalikejdbc._, SQLInterpolation._

private[item]
class ItemRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemId, Item] with ItemRepository {

  class Dao extends AbstractDao[Item] {

    override def tableName: String = "item"

    def toNamedValues(entity: Item): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id,
      'itemTypeId -> entity.itemTypeId.value.toString,
      'name -> entity.name,
      'description -> entity.description,
      'price -> entity.price,
      'supplierId -> entity.supplierId.value.toString
    )

    def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Item]): Item = {
      Item(
        id = ItemId(UUID.fromString(rs.get(n.id))),
        itemTypeId = ItemTypeId(UUID.fromString(rs.get(n.field("itemTypeId")))),
        name = rs.get(n.name),
        description = rs.stringOpt(n.field("description")),
        price = rs.get(n.price),
        supplierId = SupplierId(UUID.fromString(rs.get(n.field("supplierId"))))
      )
    }
  }

  override protected def createDao = new Dao

}
