package com.github.j5ik2o.spetstore.domain.model.customer

import com.github.j5ik2o.spetstore.domain.model.basic.SexType
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{Order, Cart, CartItem}
import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try

/**
 * ペットストアの顧客を表すエンティティ。
 *
 * @param id 識別子
 * @param status [[com.github.j5ik2o.spetstore.domain.model.customer.CustomerStatus]]
 * @param name 名前
 * @param sexType 性別
 * @param profile [[com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile]]
 * @param config [[com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig]]
 */
case class Customer
(id: CustomerId = CustomerId(),
 status: CustomerStatus.Value,
 name: String,
 sexType: Option[SexType.Value] = None,
 profile: CustomerProfile,
 config: CustomerConfig)
  extends Entity[CustomerId] {

  def addCartItem(cart: Cart, cartItem: CartItem): Cart =
    cart.addCartItem(cartItem)

  def removeCartItemByPetId(cart: Cart, petId: ItemId): Cart =
    cart.removeCartItemByPetId(petId)

  def newOrderFromCart(cart: Cart)
                      (implicit cr: CustomerRepository, ctx: EntityIOContext): Try[Order] =
    Order.fromCart(cart)

}




