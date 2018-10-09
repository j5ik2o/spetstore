package spetstore.domain.model.purchase

import cats.implicits._
import spetstore.domain.model.basic.{ Price, Quantity }
import spetstore.domain.model.item.ItemId

case class CartItem(id: CartItemId, no: Int, itemId: ItemId, quantity: Quantity, inStock: Boolean) {

  def subTotal(itemPriceResolver: ItemId => Price): Price =
    itemPriceResolver(itemId) * quantity

  def withQuantity(value: Quantity): CartItem = copy(quantity = value)

  def plusQuantityN(value: Quantity): CartItem = copy(quantity = this.quantity combine value)

  def plusOneQuantity: CartItem = plusQuantityN(Quantity.One)

  def toOrderItem(orderItemId: OrderItemId): OrderItem = OrderItem(orderItemId, no, itemId, quantity)

}
