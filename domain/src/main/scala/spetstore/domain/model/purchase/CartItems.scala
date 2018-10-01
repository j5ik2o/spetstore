package spetstore.domain.model.purchase

import cats.Monoid
import cats.implicits._
import spetstore.domain.model.basic.{Price, Quantity}
import spetstore.domain.model.item.ItemId

case class CartItems(breachEncapsulationOfValues: Seq[CartItem]) {

  def addCartItem(cartItem: CartItem): CartItems = {
    require(cartItem.quantity > Quantity(0))
    breachEncapsulationOfValues
      .find(_.itemId == cartItem.itemId).map { currentItem =>
        val newCartItem = currentItem.plusQuantityN(cartItem.quantity).ensuring(_.quantity > Quantity(0))
        copy(
          breachEncapsulationOfValues = newCartItem +: breachEncapsulationOfValues
            .filterNot(_.itemId == cartItem.itemId)
        )
      }.getOrElse {
        copy(breachEncapsulationOfValues = cartItem +: breachEncapsulationOfValues)
      }
  }

  def removeCartItemId(cartItemId: CartItemId): CartItems =
    copy(breachEncapsulationOfValues = breachEncapsulationOfValues.filterNot(_.id == cartItemId))

  def removeCartItemByItemId(itemId: ItemId): CartItems =
    breachEncapsulationOfValues
      .find(_.itemId == itemId).map { e =>
        copy(breachEncapsulationOfValues = breachEncapsulationOfValues.filterNot(_.itemId == itemId))
      }.getOrElse(this)

  def incrementQuantityByItemId(itemId: ItemId): CartItems =
    breachEncapsulationOfValues
      .find(_.itemId == itemId).map { cartItem =>
        val newCartItem = cartItem.plusOneQuantity.ensuring(_.quantity > Quantity(0))
        copy(breachEncapsulationOfValues = newCartItem +: breachEncapsulationOfValues.filterNot(_.itemId == itemId))
      }.getOrElse(this)

  def updateQuantityByItemId(itemId: ItemId, quantity: Quantity): CartItems = {
    require(quantity > Quantity(0))
    breachEncapsulationOfValues
      .find(_.itemId == itemId).map { cartItem =>
        val newCartItem = cartItem.withQuantity(quantity).ensuring(_.quantity > Quantity(0))
        copy(breachEncapsulationOfValues = newCartItem +: breachEncapsulationOfValues.filterNot(_.itemId == itemId))
      }.getOrElse(this)
  }

  val size: Int = breachEncapsulationOfValues.size

  def totalPrice(itemPriceResolver: ItemId => Price): Price =
    breachEncapsulationOfValues.foldLeft(Price.Zero) { (result, item) =>
      result combine item.subTotal(itemPriceResolver)
    }
}

object CartItems {

  implicit val monoid = new Monoid[CartItems] {
    override def empty: CartItems = CartItems(Seq.empty)

    override def combine(x: CartItems, y: CartItems): CartItems =
      CartItems(x.breachEncapsulationOfValues ++ y.breachEncapsulationOfValues)
  }

}
