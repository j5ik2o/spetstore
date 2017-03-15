package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{ Cart, CartId, CartItem, CartItemId }
import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnJDBC
import com.github.j5ik2o.spetstore.infrastructure.db.{ CartDao, CartItemDao, CartItemRecord, CartRecord }

import scala.util.Try

private[purchase] class CartRepositoryOnJDBC
    extends RepositoryOnJDBC[CartId, Cart] with CartRepository {

  override type T = CartRecord

  override protected lazy val mapper = CartRecord

  override def store(entity: Cart)(implicit ctx: Ctx): Try[(This, Cart)] = withDBSession(ctx) {
    implicit s =>
      CartDao.insertOrUpdate(entity.id.value, entity.version, convertToCartRecord(entity)).map {
        record =>
          val cartItemDao = CartItemDao(entity.id.value)
          entity.cartItems.foreach {
            cartItem =>
              cartItemDao.insertOrUpdate(cartItem.id.value, cartItem.version, convertToCartItemRecord(entity.id, cartItem)).get
          }
          (this.asInstanceOf[This], resolveById(entity.id).get)
      }
  }

  override def deleteById(identifier: CartId)(implicit ctx: Ctx): Try[(This, Cart)] = withDBSession(ctx) {
    implicit s =>
      CartDao.findById(identifier.value).flatMap {
        record =>
          val cartItemDao = CartItemDao(record.id)
          record.cartItems.foreach {
            cartItem =>
              cartItemDao.deleteById(cartItem.id).get
          }
          CartDao.deleteById(identifier.value).map {
            _ =>
              (this.asInstanceOf[This], resolveById(identifier).get)
          }
      }
  }

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Cart]] = withDBSession(ctx) {
    implicit s =>
      CartDao.findAllWithLimitOffset(limit, offset).map {
        cartRecords =>
          cartRecords.map { cartItemRecord =>
            val cartItemDao = CartItemDao(cartItemRecord.id)
            val cartItemRecords = cartItemDao.findByCartId.get
            cartItemRecord.copy(cartItems = cartItemRecords)
          }.map(convertToCart)
      }
  }

  override def resolveById(identifier: CartId)(implicit ctx: Ctx): Try[Cart] = withDBSession(ctx) {
    implicit s =>
      CartDao.findById(identifier.value).map {
        cartItemRecord =>
          val cartItemDao = CartItemDao(cartItemRecord.id)
          val cartItemRecords = cartItemDao.findByCartId.get
          cartItemRecord.copy(cartItems = cartItemRecords)
      }.map(convertToCart)
  }

  private def convertToCartRecord(model: Cart): CartRecord = {
    CartRecord(
      id = model.id.value,
      status = model.status.id,
      customerId = model.customerId.value,
      version = model.version.getOrElse(1)
    )
  }

  private def convertToCart(record: CartRecord): Cart = Cart(
    id = CartId(record.id),
    status = StatusType(record.status),
    customerId = CustomerId(record.customerId),
    cartItems = record.cartItems.map(convertToCartItem).toList,
    version = Some(record.version)
  )

  private def convertToCartItemRecord(cartId: CartId, model: CartItem): CartItemRecord = {
    CartItemRecord(
      id = model.id.value,
      no = model.no,
      status = model.status.id,
      cartId = cartId.value,
      itemId = model.itemId.value,
      quantity = model.quantity,
      inStock = model.inStock,
      version = model.version.getOrElse(1)
    )
  }

  private def convertToCartItem(record: CartItemRecord): CartItem = CartItem(
    id = CartItemId(record.id),
    no = record.no,
    status = StatusType(record.status),
    itemId = ItemId(record.itemId),
    quantity = record.quantity,
    inStock = record.inStock,
    version = Some(record.version)
  )

}
