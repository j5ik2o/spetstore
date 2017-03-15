package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, RepositoryOnJDBC }
import com.github.j5ik2o.spetstore.domain.model.item.{ Inventory, InventoryId }
import com.github.j5ik2o.spetstore.infrastructure.db.InventoryRecord
import scala.util.Try

private[item] class InventoryRepositoryOnJDBC
    extends RepositoryOnJDBC[InventoryId, Inventory] with InventoryRepository {
  override type T = InventoryRecord

  override protected lazy val mapper = InventoryRecord

  override def deleteById(identifier: InventoryId)(implicit ctx: Ctx): Try[(This, Inventory)] = ???

  override def store(entity: Inventory)(implicit ctx: Ctx): Try[(InventoryRepositoryOnJDBC#This, Inventory)] = ???

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Inventory]] = ???

  override def resolveById(identifier: InventoryId)(implicit ctx: Ctx): Try[Inventory] = ???
}
