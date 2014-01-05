package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.domain.account.{Account, AccountRepository, AccountId}
import com.github.j5ik2o.spetstore.domain.item.{ItemId, Item}
import com.github.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try

/**
 * ショッピングカートを表すエンティティ。
 *
 * @param cartItems [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]のリスト
 */
case class Cart
(id: CartId = CartId(),
 accountId: AccountId,
 cartItems: List[CartItem]) extends Entity[CartId] {

  /**
   * [[com.github.j5ik2o.spetstore.domain.account.Account]]を取得する。
   *
   * @param ar [[com.github.j5ik2o.spetstore.domain.account.AccountRepository]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.account.Account]]
   */
  def account(implicit ar: AccountRepository, ctx: EntityIOContext): Try[Account] =
    ar.resolve(accountId)

  /**
   * [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]の個数。
   */
  val sizeOfCartItems = cartItems.size

  /**
   * [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]の総数。
   */
  val quantityOfCartItems = cartItems.foldLeft(0)(_ + _.quantity)

  /**
   * 合計金額。
   */
  lazy val totalPrice = cartItems.foldLeft(BigDecimal(0))(_ + _.subTotalPrice)

  /**
   * [[com.github.j5ik2o.spetstore.domain.item.ItemId]]が含まれるかを検証する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.item.ItemId]]
   * @return 含まれる場合はtrue
   */
  def containsItemId(itemId: ItemId): Boolean =
    cartItems.exists {
      _.item.id == itemId
    }

  /**
   * このカートに[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]を追加する。
   *
   * @param cartItem [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def addCartItem(cartItem: CartItem): Cart = {
    require(cartItem.quantity > 0)
    cartItems.find(_.item == cartItem.item).map {
      currentItem =>
        val newCartItem = currentItem.addQuantity(cartItem.quantity).ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.item == cartItem.item))
    }.getOrElse {
      copy(cartItems = cartItem :: cartItems)
    }
  }

  /**
   * このカートに[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]を追加する。
   *
   * @param item [[com.github.j5ik2o.spetstore.domain.item.Item]]
   * @param quantity 個数
   * @param isInStock ストックする場合true
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def addCartItem(item: Item, quantity: Int, isInStock: Boolean): Cart =
    addCartItem(CartItem(item, quantity, isInStock))

  /**
   * [[com.github.j5ik2o.spetstore.domain.item.ItemId]]を使って
   * [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]を
   * 削除する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.item.ItemId]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def removeItemById(itemId: ItemId): Cart =
    cartItems.find(_.item.id == itemId).map {
      e =>
        copy(cartItems = cartItems.filterNot(_.item.id == itemId))
    }.getOrElse(this)

  /**
   * 特定の[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]の数量を
   * インクリメントする。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.item.ItemId]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def incrementQuantityByItemId(itemId: ItemId): Cart =
    cartItems.find(_.item.id == itemId).map {
      cartItem =>
        val newCartItem = cartItem.incrementQuantity.ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.item.id == itemId))
    }.getOrElse(this)

  /**
   * 特定の[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]の数量を更新する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.item.ItemId]]
   * @param quantity 数量
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def updateQuantityByItemId(itemId: ItemId, quantity: Int): Cart = {
    require(quantity > 0)
    cartItems.find(_.item.id == itemId).map {
      cartItem =>
        val newCartItem = cartItem.withQuantity(quantity).ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.item.id == itemId))
    }.getOrElse(this)
  }

}
