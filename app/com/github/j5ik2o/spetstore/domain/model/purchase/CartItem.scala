package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.model.pet.Pet

/**
 * 注文する商品を表す値オブジェクト。
 *
 * @param pet [[com.github.j5ik2o.spetstore.domain.model.pet.Pet]]
 * @param quantity 数量
 * @param inStock 後で購入する場合true
 */
case class CartItem(pet: Pet, quantity: Int, inStock: Boolean) {

  /**
   * 小計。
   */
  lazy val subTotalPrice: BigDecimal = pet.price * quantity

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
