package spetstore.domain.model.purchase

import com.github.j5ik2o.dddbase.Aggregate
import org.sisioh.baseunits.scala.time.TimePoint
import org.sisioh.baseunits.scala.timeutil.Clock
import spetstore.domain.model.basic.{ Price, Quantity, StatusType }
import spetstore.domain.model.item.ItemId
import spetstore.domain.model.{ UserAccount, UserAccountId }

import scala.reflect.{ classTag, ClassTag }

case class Cart(id: CartId,
                status: StatusType,
                userAccountId: UserAccountId,
                cartItems: CartItems,
                createdAt: TimePoint,
                updatedAt: TimePoint)
    extends Aggregate {

  override type IdType        = CartId
  override type AggregateType = Cart
  override protected val tag: ClassTag[Cart] = classTag[Cart]

  val size: Int = cartItems.size

  def totalPrice(itemPriceResolver: ItemId => Price): Price =
    cartItems.totalPrice(itemPriceResolver)

  def addCartItem(cartItem: CartItem): Cart =
    copy(cartItems = cartItems.addCartItem(cartItem))

  def removeCartItem(cartItemId: CartItemId): Cart =
    copy(cartItems = cartItems.removeCartItemId(cartItemId))

  def removeCartItemByItemId(itemId: ItemId): Cart =
    copy(cartItems = cartItems.removeCartItemByItemId(itemId))

  def incrementQuantityByItemId(itemId: ItemId): Cart =
    copy(cartItems = cartItems.incrementQuantityByItemId(itemId))

  def updateQuantityByItemId(itemId: ItemId, quantity: Quantity): Cart =
    copy(cartItems = cartItems.updateQuantityByItemId(itemId, quantity))

  def cleanUp(orderId: OrderId, userAccountResolver: UserAccountId => UserAccount): Order = {
    val customer = userAccountResolver(userAccountId)
    Order(
      id = orderId,
      status,
      Clock.now.asCalendarDate(),
      userAccountId,
      null,
      null,
      null,
      Clock.now,
      None
    )
  }

}
