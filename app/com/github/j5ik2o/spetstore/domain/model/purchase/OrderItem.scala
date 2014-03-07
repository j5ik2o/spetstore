package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.model.item.Item

/**
 * 注文する商品を表す値オブジェクト。
 *
 * @param item [[com.github.j5ik2o.spetstore.domain.model.item.Item]]
 * @param quantity 数量
 */
case class OrderItem(item: Item, quantity: Int) {

  /**
   * 小計。
   */
  val subTotalPrice: BigDecimal = item.price * quantity

}

/**
 * コンパニオンオブジェクト。
 */
object OrderItem {

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]から
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]を
   * 生成する。
   *
   * @param cartItem [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]
   * @return [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]
   */
  def fromCartItem(cartItem: CartItem): OrderItem =
    OrderItem(cartItem.item, cartItem.quantity)

}
