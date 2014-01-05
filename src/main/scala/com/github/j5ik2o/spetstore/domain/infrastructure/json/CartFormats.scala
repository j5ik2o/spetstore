package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.item.Item
import com.github.j5ik2o.spetstore.domain.purchase.CartItem
import org.json4s.DefaultReaders._
import org.json4s._
import ItemFormats._

object CartFormats {

  implicit object CartItemFormat extends Writer[CartItem] with Reader[CartItem] {

    def write(obj: CartItem): JValue =
      JObject(
        JField("item", obj.item.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("isInStock", JBool(obj.inStock))
      )

    def read(value: JValue): CartItem = CartItem(
      item = (value \ "item").as[Item],
      quantity = (value \ "quantity").as[Int],
      inStock = (value \ "isInStock").as[Boolean]
    )

  }

}
