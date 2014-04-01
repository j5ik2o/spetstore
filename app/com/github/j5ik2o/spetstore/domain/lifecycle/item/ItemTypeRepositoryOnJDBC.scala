package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.item.{ItemType, ItemTypeId}
import com.github.j5ik2o.spetstore.infrastructure.db.ItemTypeRecord
import scala.util.Try


private[item]
class ItemTypeRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemTypeId, ItemType] with ItemTypeRepository {

  override type T = ItemTypeRecord

  override protected val mapper = ItemTypeRecord

  override def deleteByIdentifier(identifier: ItemTypeId)(implicit ctx: Ctx): Try[(This, ItemType)] = ???

  override def storeEntity(entity: ItemType)(implicit ctx: Ctx): Try[(This, ItemType)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[ItemType]] = ???

  override def resolveEntity(identifier: ItemTypeId)(implicit ctx: Ctx): Try[ItemType] = ???
}
