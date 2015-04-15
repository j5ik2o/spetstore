package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.{EntityIOContext, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.item.{Item, ItemId}
import com.github.j5ik2o.spetstore.infrastructure.db.ItemRecord
import scala.util.Try

private[item]
class ItemRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemId, Item] with ItemRepository {

  override type T = ItemRecord

  override protected val mapper = ItemRecord

  override def deleteById(identifier: ItemId)(implicit ctx: Ctx): Try[(This, Item)] = ???

  override def store(entity: Item)(implicit ctx: Ctx): Try[(ItemRepositoryOnJDBC#This, Item)] = ???

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Item]] = ???

  override def resolveById(identifier: ItemId)(implicit ctx: Ctx): Try[Item] = ???
}
