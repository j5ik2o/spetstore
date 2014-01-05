package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.domain.customer.{Customer, CustomerRepository, CustomerId}
import com.github.j5ik2o.spetstore.domain.pet.{PetId, Pet}
import com.github.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try

/**
 * ショッピングカートを表すエンティティ。
 *
 * @param cartItems [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]のリスト
 */
case class Cart
(id: CartId = CartId(),
 customerId: CustomerId,
 cartItems: List[CartItem]) extends Entity[CartId] {

  /**
   * [[com.github.j5ik2o.spetstore.domain.customer.Customer]]を取得する。
   *
   * @param cr [[com.github.j5ik2o.spetstore.domain.customer.CustomerRepository]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.customer.Customer]]
   */
  def customer(implicit cr: CustomerRepository, ctx: EntityIOContext): Try[Customer] =
    cr.resolve(customerId)

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
   * [[com.github.j5ik2o.spetstore.domain.pet.PetId]]が含まれるかを検証する。
   *
   * @param itemId [[com.github.j5ik2o.spetstore.domain.pet.PetId]]
   * @return 含まれる場合はtrue
   */
  def containsItemId(itemId: PetId): Boolean =
    cartItems.exists {
      _.pet.id == itemId
    }

  /**
   * このカートに[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]を追加する。
   *
   * @param cartItem [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def addCartItem(cartItem: CartItem): Cart = {
    require(cartItem.quantity > 0)
    cartItems.find(_.pet == cartItem.pet).map {
      currentItem =>
        val newCartItem = currentItem.addQuantity(cartItem.quantity).ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.pet == cartItem.pet))
    }.getOrElse {
      copy(cartItems = cartItem :: cartItems)
    }
  }

  /**
   * このカートに[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]を追加する。
   *
   * @param pet [[com.github.j5ik2o.spetstore.domain.pet.Pet]]
   * @param quantity 個数
   * @param isInStock ストックする場合true
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def addCartItem(pet: Pet, quantity: Int, isInStock: Boolean): Cart =
    addCartItem(CartItem(pet, quantity, isInStock))

  /**
   * [[com.github.j5ik2o.spetstore.domain.pet.PetId]]を使って
   * [[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]を
   * 削除する。
   *
   * @param petId [[com.github.j5ik2o.spetstore.domain.pet.PetId]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def removeItemById(petId: PetId): Cart =
    cartItems.find(_.pet.id == petId).map {
      e =>
        copy(cartItems = cartItems.filterNot(_.pet.id == petId))
    }.getOrElse(this)

  /**
   * 特定の[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]の数量を
   * インクリメントする。
   *
   * @param petId [[com.github.j5ik2o.spetstore.domain.pet.PetId]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def incrementQuantityByItemId(petId: PetId): Cart =
    cartItems.find(_.pet.id == petId).map {
      cartItem =>
        val newCartItem = cartItem.incrementQuantity.ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.pet.id == petId))
    }.getOrElse(this)

  /**
   * 特定の[[com.github.j5ik2o.spetstore.domain.purchase.CartItem]]の数量を更新する。
   *
   * @param petId [[com.github.j5ik2o.spetstore.domain.pet.PetId]]
   * @param quantity 数量
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.purchase.Cart]]
   */
  def updateQuantityByItemId(petId: PetId, quantity: Int): Cart = {
    require(quantity > 0)
    cartItems.find(_.pet.id == petId).map {
      cartItem =>
        val newCartItem = cartItem.withQuantity(quantity).ensuring(_.quantity > 0)
        copy(cartItems = newCartItem :: cartItems.filterNot(_.pet.id == petId))
    }.getOrElse(this)
  }

}
