package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import com.github.j5ik2o.spetstore.domain.model.item.{ItemId, Inventory, InventoryId}
import java.util.UUID
import scalikejdbc._, SQLInterpolation._

private[item]
class InventoryRepositoryOnJDBC
  extends RepositoryOnJDBC[InventoryId, Inventory] with InventoryRepository {

  class Dao extends AbstractDao[Inventory] {
    override def defaultAlias = createAlias("i")

    override val connectionPoolName = 'inventory

    override val tableName = "inventory"

    def extract(rs: WrappedResultSet, p: SQLInterpolation.ResultName[Inventory]): Inventory =
      Inventory(
        id = InventoryId(UUID.fromString(rs.get(p.id))),
        itemId = ItemId(UUID.fromString(rs.get(p.itemId))),
        quantity = rs.get(p.quantity)
      )

    def toNamedValues(entity: Inventory): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id.value,
      'itemId -> entity.itemId.value,
      'quantity -> entity.quantity
    )

  }

  override protected def createDao = new Dao

}
