package com.github.j5ik2o.spetstore.domain.support.json

import IdentifierFormats._
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{CartItemId, CartItem}
import org.json4s.DefaultReaders._
import org.json4s._

object CartFormats {

  implicit object CartItemFormat extends Writer[CartItem] with Reader[CartItem] {

    def read(value: JValue): CartItem = CartItem(
      id = CartItemId((value \ "id").as[String].toLong),
      status = StatusType((value \ "status").as[Int]),
      no = (value \ "no").as[Int],
      itemId = (value \ "itemId").as[ItemId],
      quantity = (value \ "quantity").as[Int],
      inStock = (value \ "isInStock").as[Boolean],
      version = (value \ "version").as[Option[String]].map(_.toLong)
    )

    def write(obj: CartItem): JValue =
      JObject(
        JField("id", JString(obj.id.value.toString)),
        JField("status", JInt(obj.status.id)),
        JField("no", JInt(obj.no)),
        JField("itemId", obj.itemId.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("isInStock", JBool(obj.inStock)),
        JField("version", obj.version.map(e => JString(e.toString)).getOrElse(JNull))
      )

  }

}
