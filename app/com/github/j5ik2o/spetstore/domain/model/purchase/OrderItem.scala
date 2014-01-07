package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.model.pet.Pet

/**
 * 注文する商品を表す値オブジェクト。
 *
 * @param pet [[com.github.j5ik2o.spetstore.domain.model.pet.Pet]]
 * @param quantity 数量
 */
case class OrderItem(pet: Pet, quantity: Int) {

  /**
   * 小計。
   */
  val subTotalPrice: BigDecimal = pet.price * quantity

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
    OrderItem(cartItem.pet, cartItem.quantity)

}
