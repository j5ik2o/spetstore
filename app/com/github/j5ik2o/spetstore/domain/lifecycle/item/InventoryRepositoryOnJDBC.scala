package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.item.{ItemId, Inventory, InventoryId}
import java.util.UUID
import scalikejdbc._, SQLInterpolation._
import scala.util.Try
import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.db.InventoryRecord

private[item]
class InventoryRepositoryOnJDBC
  extends RepositoryOnJDBC[InventoryId, Inventory] with InventoryRepository {
  override type T = InventoryRecord

  override protected val mapper = InventoryRecord

  override def deleteByIdentifier(identifier: InventoryId)(implicit ctx: InventoryRepositoryOnJDBC#Ctx): Try[(InventoryRepositoryOnJDBC#This, Inventory)] = ???

  override def storeEntity(entity: Inventory)(implicit ctx: InventoryRepositoryOnJDBC#Ctx): Try[(InventoryRepositoryOnJDBC#This, Inventory)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: EntityIOContext): Try[Seq[Inventory]] = ???

  override def resolveEntity(identifier: InventoryId)(implicit ctx: InventoryRepositoryOnJDBC#Ctx): Try[Inventory] = ???
}
