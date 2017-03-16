package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, Entity }
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemTypeRepository
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import scala.util.Try

/**
 * ペットを表すエンティティ。
 *
 * @param id 識別子
 * @param itemTypeId [[com.github.j5ik2o.spetstore.domain.model.item.ItemTypeId]]
 * @param name 名前
 * @param description 説明
 * @param price 価格
 */
case class Item(
  id: ItemId,
  status: StatusType.Value,
  itemTypeId: ItemTypeId,
  name: String,
  description: Option[String] = None,
  price: BigDecimal,
  supplierId: SupplierId,
  version: Option[Long]
)
    extends Entity[ItemId] {

  override def canEqual(other: Any) = other.isInstanceOf[Item]

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.item.ItemType]]を取得する。
   *
   * @param itr [[com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemTypeRepository]]
   * @param ctx [[com.github.j5ik2o.spetstore.domain.support.support.EntityIOContext]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.model.item.ItemType]]
   */
  def itemType(implicit itr: ItemTypeRepository, ctx: EntityIOContext): Try[ItemType] =
    itr.resolveById(itemTypeId)

  override def withVersion(version: Long): Entity[ItemId] = copy(version = Some(version))

}

