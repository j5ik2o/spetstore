package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.domain.item.Item

/**
 * 注文する商品を表す値オブジェクト。
 *
 * @param item [[com.github.j5ik2o.spetstore.domain.item.Item]]
 * @param quantity 数量
 * @param inStock 後で購入する場合true
 */
case class CartItem(item: Item, quantity: Int, inStock: Boolean) {

  /**
   * 小計。
   */
  lazy val subTotalPrice: BigDecimal = item.price * quantity

  /**
   * 数量をインクリメントする。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]
   */
  def incrementQuantity: CartItem = addQuantity(1)

  /**
   * 数量を追加する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]
   */
  def addQuantity(otherQuantity: Int): CartItem = copy(quantity = quantity + otherQuantity)

  /**
   * 数量を更新する。
   *
   * @param quantity 数量
   * @return [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]
   */
  def withQuantity(quantity: Int): CartItem = copy(quantity = quantity)

}
