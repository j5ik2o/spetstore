package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.item.{SupplierId, ItemTypeId, Item, ItemId}
import java.util.UUID
import scalikejdbc._, SQLInterpolation._
import scala.util.Try
import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.db.ItemRecord

private[item]
class ItemRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemId, Item] with ItemRepository {

  override type T = ItemRecord

  override protected val mapper = ItemRecord

  override def deleteByIdentifier(identifier: ItemId)(implicit ctx: ItemRepositoryOnJDBC#Ctx): Try[(ItemRepositoryOnJDBC#This, Item)] = ???

  override def storeEntity(entity: Item)(implicit ctx: ItemRepositoryOnJDBC#Ctx): Try[(ItemRepositoryOnJDBC#This, Item)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: EntityIOContext): Try[Seq[Item]] = ???

  override def resolveEntity(identifier: ItemId)(implicit ctx: ItemRepositoryOnJDBC#Ctx): Try[Item] = ???
}
