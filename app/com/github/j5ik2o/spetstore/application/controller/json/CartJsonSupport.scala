package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.application.controller.CartController
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{CartItemId, Cart, CartId, CartItem}
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CartItemJson(id: Option[String], no: Int, itemId: String, quantity: Int, inStock: Boolean, version: Option[Long])

case class CartJson(id: Option[String], customerId: String, cartItems: Seq[CartItemJson], version: Option[Long])

/**
 * [[CartJson]]のためのトレイト。
 */
trait CartJsonSupport {
  this: CartController =>

  protected def convertToEntity(json: CartItemJson): CartItem = CartItem(
    id = CartItemId(json.id.map(_.toLong).get),
    no = json.no,
    status = StatusType.Enabled,
    itemId = ItemId(json.itemId.toLong),
    quantity = json.quantity,
    inStock = json.inStock,
    version = json.version
  )

  protected def convertToEntityWithoutId(json: CartItemJson): CartItem = CartItem(
    id = CartItemId(identifierService.generate),
    no = json.no,
    status = StatusType.Enabled,
    itemId = ItemId(json.itemId.toLong),
    quantity = json.quantity,
    inStock = json.inStock,
    version = json.version
  )

  protected def convertToEntity(json: CartJson): Cart = Cart(
    id = CartId(json.id.map(_.toLong).get),
    status = StatusType.Enabled,
    customerId = CustomerId(json.customerId.toLong),
    cartItems = json.cartItems.map(convertToEntity).toList,
    version = json.version
  )

  protected def convertToEntityWithoutId(json: CartJson): Cart = Cart(
    id = CartId(identifierService.generate),
    status = StatusType.Enabled,
    customerId = CustomerId(json.customerId.toLong),
    cartItems = json.cartItems.map(convertToEntityWithoutId).toList,
    version = json.version
  )

  implicit object ItemConverter extends Reads[CartItemJson] with Writes[CartItem] {

    override def writes(o: CartItem): JsValue = JsObject(
      Seq(
        "id" -> JsString(o.id.value.toString),
        "no" -> JsNumber(o.no),
        "itemId" -> JsNumber(o.itemId.value),
        "quantity" -> JsNumber(o.quantity),
        "inStock" -> JsBoolean(o.inStock),
        "version" -> o.version.map(e => JsString(e.toString)).getOrElse(JsNull)
      )
    )

    override def reads(json: JsValue): JsResult[CartItemJson] =
      ((__ \ 'id).readNullable[String] and
        (__ \ 'no).read[Int] and
        (__ \ 'itemId).read[String] and
        (__ \ 'quantity).read[Int] and
        (__ \ 'inStock).read[Boolean] and
        (__ \ 'version).readNullable[String].map(_.map(_.toLong)))(CartItemJson.apply _).reads(json)

  }

  implicit object JsonConverter extends Reads[CartJson] with Writes[Cart] {

    override def writes(o: Cart): JsValue = JsObject(
      Seq(
        "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
        "customerId" -> JsString(o.customerId.value.toString),
        "cartItems" -> JsArray(o.cartItems.map(Json.toJson(_))),
        "version" -> JsString(o.version.toString)
      )
    )

    override def reads(json: JsValue): JsResult[CartJson] =
      ((__ \ 'id).readNullable[String] and
        (__ \ 'customerId).read[String] and
        (__ \ 'cartItems).read[Seq[CartItemJson]] and
        (__ \ 'version).readNullable[String].map(_.map(_.toLong)))(CartJson.apply _).reads(json)
  }

}
