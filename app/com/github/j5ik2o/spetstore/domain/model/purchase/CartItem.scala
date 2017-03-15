package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.support.support.EntityIOContext
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import scala.util.Try

/**
 * 注文する商品を表す値オブジェクト。
 *
 * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.Item]]のID
 * @param quantity 数量
 * @param inStock 後で購入する場合true
 */
case class CartItem(
    id: CartItemId,
    status: StatusType.Value,
    no: Int,
    itemId: ItemId,
    quantity: Int,
    inStock: Boolean,
    version: Option[Long]
) {

  /**
   * 小計。
   */
  def subTotalPrice(implicit ir: ItemRepository, ctx: EntityIOContext): Try[BigDecimal] =
    ir.resolveById(itemId).map(_.price * quantity)

  /**
   * 数量をインクリメントする。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]
   */
  def incrementQuantity: CartItem = addQuantity(1)

  /**
   * 数量を追加する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]
   */
  def addQuantity(otherQuantity: Int): CartItem = copy(quantity = quantity + otherQuantity)

  /**
   * 数量を更新する。
   *
   * @param quantity 数量
   * @return [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]
   */
  def withQuantity(quantity: Int): CartItem = copy(quantity = quantity)

}
