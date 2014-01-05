package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try

/**
 * 商品を表すエンティティ。
 *
 * TODO 商品と在庫の概念を分ける
 * 
 * @param id 識別子
 * @param itemTypeId [[com.github.j5ik2o.spetstore.domain.item.ItemTypeId]]
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

  /**
   * [[com.github.j5ik2o.spetstore.domain.item.ItemType]]を取得する。
   *
   * @param itr [[com.github.j5ik2o.spetstore.domain.item.ItemTypeRepository]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.item.ItemType]]
   */
  def itemType(implicit itr: ItemTypeRepository, ctx: EntityIOContext): Try[ItemType] =
    itr.resolve(itemTypeId)

}



