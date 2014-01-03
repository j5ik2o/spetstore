package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try

/**
 * 商品を表すエンティティ。
 * 
 * @param id 識別子
 * @param itemTypeId [[com.j5ik2o.spetstore.domain.item.ItemTypeId]]
 * @param name 名前
 * @param description 説明
 * @param price 価格
 * @param quantity 在庫数量
 */
case class Item
(id: ItemId = ItemId(),
 itemTypeId: ItemTypeId,
 name: String,
 description: Option[String] = None,
 price: BigDecimal,
 quantity: Int = 1)
  extends Entity[ItemId] {

  def itemType(implicit itr: ItemTypeRepository, ctx: EntityIOContext): Try[ItemType] =
    itr.resolve(itemTypeId)

  def total: BigDecimal = price * quantity



}



