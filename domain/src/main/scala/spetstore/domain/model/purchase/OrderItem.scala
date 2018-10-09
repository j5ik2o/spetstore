package spetstore.domain.model.purchase

import spetstore.domain.model.basic.{ Price, Quantity }
import spetstore.domain.model.item.ItemId

case class OrderItem(id: OrderItemId, no: Int, itemId: ItemId, quantity: Quantity) {

  def subTotal(itemPriceResolver: ItemId => Price): Price =
    itemPriceResolver(itemId) * quantity

}
