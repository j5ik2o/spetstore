package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, Entity }
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.{ Customer, CustomerId }
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import scala.util.Try

/**
 * ショッピングカートを表すエンティティ。
 *
 * @param cartItems [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]のリスト
 */
case class Cart(
    id: CartId,
    status: StatusType.Value,
    customerId: CustomerId,
    cartItems: List[CartItem],
    version: Option[Long]
) extends Entity[CartId] {

  override def canEqual(other: Any) = other.isInstanceOf[Cart]

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]を取得する。
   *
   * @param cr [[CustomerRepository]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]
   */
  def customer(implicit cr: CustomerRepository, ctx: EntityIOContext): Try[Customer] =
    cr.resolveById(customerId)

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]の個数。
   */
  val sizeOfCartItems = cartItems.size

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]の総数。
   */
  val quantityOfCartItems = cartItems.foldLeft(0)(_ + _.quantity)

  /**
   * 合計金額。
   */
  def totalPrice(implicit ir: ItemRepository, ctx: EntityIOContext) = Try {
    cartItems.foldLeft(BigDecimal(0))((l, r) => l + r.subTotalPrice.get)
  }

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]が含まれるかを検証する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
   * @return 含まれる場合はtrue
   */
  def containsItemId(itemId: ItemId): Boolean =
    cartItems.exists {
      _.itemId == itemId
    }

  /**
   * このカートに[[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]を追加する。
   *
   * @param cartItem [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]
   */
  def addCartItem(cartItem: CartItem): Cart = {
    require(cartItem.quantity > 0)
    cartItems.find(_.itemId == cartItem.itemId).map {
      currentItem =>
        val newCartItem = currentItem.addQuantity(cartItem.quantity).ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.itemId == cartItem.itemId))
    }.getOrElse {
      copy(cartItems = cartItem :: cartItems)
    }
  }

  /**
   * このカートに[[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]を追加する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
   * @param quantity 個数
   * @param isInStock ストックする場合true
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]
   */
  def addCartItem(cartItemId: CartItemId, itemId: ItemId, quantity: Int, isInStock: Boolean = false): Cart =
    addCartItem(CartItem(cartItemId, StatusType.Enabled, cartItems.size + 1, itemId, quantity, isInStock, None))

  def removeCartItemId(cartItemId: CartItemId): Cart =
    copy(
      cartItems = cartItems.filterNot(_.id == cartItemId)
    )

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]を使って
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]を
   * 削除する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]
   */
  def removeCartItemByItemId(itemId: ItemId): Cart =
    cartItems.find(_.itemId == itemId).map {
      e =>
        copy(cartItems = cartItems.filterNot(_.itemId == itemId))
    }.getOrElse(this)

  /**
   * 特定の[[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]の数量を
   * インクリメントする。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]
   */
  def incrementQuantityByItemId(itemId: ItemId): Cart =
    cartItems.find(_.itemId == itemId).map {
      cartItem =>
        val newCartItem = cartItem.incrementQuantity.ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.itemId == itemId))
    }.getOrElse(this)

  /**
   * 特定の[[com.github.j5ik2o.spetstore.domain.model.purchase.CartItem]]の数量を更新する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.model.item.ItemId]]
   * @param quantity 数量
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]
   */
  def updateQuantityByItemId(itemId: ItemId, quantity: Int): Cart = {
    require(quantity > 0)
    cartItems.find(_.itemId == itemId).map {
      cartItem =>
        val newCartItem = cartItem.withQuantity(quantity).ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.itemId == itemId))
    }.getOrElse(this)
  }

  override def withVersion(version: Long): Entity[CartId] = copy(version = Some(version))
}
