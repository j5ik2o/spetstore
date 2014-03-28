package com.github.j5ik2o.spetstore.domain.model.customer

import com.github.j5ik2o.spetstore.domain.model.basic.{StatusType, SexType}
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{Order, Cart, CartItem}
import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository

/**
 * ペットストアの顧客を表すエンティティ。
 *
 * @param id 識別子
 * @param status [[StatusType]]
 * @param name 名前
 * @param sexType 性別
 * @param profile [[com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile]]
 * @param config [[com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig]]
 */
case class Customer
(id: CustomerId = CustomerId(),
 status: StatusType.Value,
 name: String,
 sexType: SexType.Value,
 profile: CustomerProfile,
 config: CustomerConfig)
  extends Entity[CustomerId] {

  def addCartItem(cart: Cart, cartItem: CartItem): Cart =
    cart.addCartItem(cartItem)

  def removeCartItemByPetId(cart: Cart, itemId: ItemId): Cart =
    cart.removeCartItemByPetId(itemId)

  def newOrderFromCart(cart: Cart)
                      (implicit cr: CustomerRepository, ctx: EntityIOContext): Try[Order] =
    Order.fromCart(cart)

}




