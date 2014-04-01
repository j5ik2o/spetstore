package com.github.j5ik2o.spetstore.application.json

import com.github.j5ik2o.spetstore.application.controller.CartController
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.Cart
import com.github.j5ik2o.spetstore.domain.model.purchase.CartId
import com.github.j5ik2o.spetstore.domain.model.purchase.CartItem
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CartItemJson(no: Long, itemId: String, quantity: Int, inStock: Boolean)

case class CartJson(id: Option[String], customerId: String, cartItems: Seq[CartItemJson])

trait CartJsonSupport {
  this: CartController =>

  protected def convertToEntity(json: CartItemJson): CartItem = CartItem(
    no = json.no,
    status = StatusType.Enabled,
    itemId = ItemId(json.itemId.toLong),
    quantity = json.quantity,
    inStock = json.inStock
  )

  protected def convertToEntity(json: CartJson): Cart = Cart(
    id = CartId(json.id.map(_.toLong).get),
    status = StatusType.Enabled,
    customerId = CustomerId(json.customerId.toLong),
    cartItems = json.cartItems.map(convertToEntity).toList
  )

  protected def convertToEntityWithoutId(json: CartJson): Cart = Cart(
    id = CartId(identifierService.generate),
    status = StatusType.Enabled,
    customerId = CustomerId(json.customerId.toLong),
    cartItems = json.cartItems.map(convertToEntity).toList
  )

  implicit object ItemConverter extends Reads[CartItemJson] with Writes[CartItem] {

    override def writes(o: CartItem): JsValue = JsObject(
      Seq(
        "no" -> JsNumber(o.no),
        "itemId" -> JsNumber(o.itemId.value),
        "quantity" -> JsNumber(o.quantity),
        "inStock" -> JsBoolean(o.inStock)
      )
    )

    override def reads(json: JsValue): JsResult[CartItemJson] =
      ((__ \ 'no).read[Long] and
        (__ \ 'itemId).read[String] and
        (__ \ 'quantity).read[Int] and
        (__ \ 'inStock).read[Boolean])(CartItemJson.apply _).reads(json)

  }

  implicit object JsonConverter extends Reads[CartJson] with Writes[Cart] {

    override def writes(o: Cart): JsValue = JsObject(
      Seq(
        "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
        "customerId" -> JsString(o.customerId.value.toString),
        "cartItems" -> JsArray(o.cartItems.map(Json.toJson(_)))
      )
    )

    override def reads(json: JsValue): JsResult[CartJson] =
      ((__ \ 'id).readNullable[String] and
        (__ \ 'customerId).read[String] and
        (__ \ 'cartItems).read[Seq[CartItemJson]])(CartJson.apply _).reads(json)
  }

}
