package com.j5ik2o.spetstore.domain.purchase

import com.j5ik2o.spetstore.domain.item.Item

/**
 * 注文する商品を表す値オブジェクト。
 *
 * @param item [[com.j5ik2o.spetstore.domain.item.Item]]
 * @param quantity 数量
 */
case class OrderItem(item: Item, quantity: Int) {

  /**
   * 小計。
   */
  val subTotal: BigDecimal = item.price * quantity

}

object OrderItem {

  def fromCartItem(cartItem: CartItem): OrderItem = {
    OrderItem(cartItem.item, cartItem.quantity)
  }

}
