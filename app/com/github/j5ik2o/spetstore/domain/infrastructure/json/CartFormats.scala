package com.github.j5ik2o.spetstore.domain.infrastructure.json

import IdentifierFormats._
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.CartItem
import org.json4s.DefaultReaders._
import org.json4s._

object CartFormats {

  implicit object CartItemFormat extends Writer[CartItem] with Reader[CartItem] {

    def read(value: JValue): CartItem = CartItem(
      no = (value \ "no").as[Long],
      status = StatusType((value \ "status").as[Int]),
      itemId = (value \ "itemId").as[ItemId],
      quantity = (value \ "quantity").as[Int],
      inStock = (value \ "isInStock").as[Boolean]
    )

    def write(obj: CartItem): JValue =
      JObject(
        JField("no", JInt(obj.no)),
        JField("status", JInt(obj.status.id)),
        JField("itemId", obj.itemId.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("isInStock", JBool(obj.inStock))
      )

  }

}
