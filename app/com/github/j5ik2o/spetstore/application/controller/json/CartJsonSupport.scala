package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{ Cart, CartId, CartItem, CartItemId }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

case class CartItemJson(id: Option[String], no: Int, itemId: Long, quantity: Int, inStock: Boolean, version: Option[Long])

case class CartJson(id: Option[String], customerId: Long, cartItems: Seq[CartItemJson], version: Option[Long])

/**
 * [[CartJson]]のためのトレイト。
 */
trait CartJsonSupport {

  val identifierService: IdentifierService

  protected def convertToEntity(json: CartItemJson): CartItem = CartItem(
    id = CartItemId(json.id.map(_.toLong).get),
    no = json.no,
    status = StatusType.Enabled,
    itemId = ItemId(json.itemId),
    quantity = json.quantity,
    inStock = json.inStock,
    version = json.version
  )

  protected def convertToJson(entity: CartItem): CartItemJson = CartItemJson(
    id = Some(entity.id.value.toString),
    no = entity.no,
    itemId = entity.itemId.value,
    quantity = entity.quantity,
    inStock = entity.inStock,
    version = entity.version
  )

  protected def convertToEntityWithoutId(json: CartItemJson): CartItem = CartItem(
    id = CartItemId(identifierService.generate),
    no = json.no,
    status = StatusType.Enabled,
    itemId = ItemId(json.itemId),
    quantity = json.quantity,
    inStock = json.inStock,
    version = json.version
  )

  protected def convertToEntity(json: CartJson): Cart = Cart(
    id = CartId(json.id.map(_.toLong).get),
    status = StatusType.Enabled,
    customerId = CustomerId(json.customerId),
    cartItems = json.cartItems.map(convertToEntity).toList,
    version = json.version
  )

  protected def convertToEntityWithoutId(json: CartJson): Cart = Cart(
    id = CartId(identifierService.generate),
    status = StatusType.Enabled,
    customerId = CustomerId(json.customerId),
    cartItems = json.cartItems.map(convertToEntityWithoutId).toList,
    version = json.version
  )

  protected def convertToJson(entity: Cart): CartJson = CartJson(
    id = Some(entity.id.value.toString),
    customerId = entity.customerId.value,
    cartItems = entity.cartItems.map(convertToJson),
    version = entity.version
  )

}
