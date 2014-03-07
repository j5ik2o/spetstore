package com.github.j5ik2o.spetstore.domain.infrastructure.json

import PetFormats._
import com.github.j5ik2o.spetstore.domain.model.item.Item
import com.github.j5ik2o.spetstore.domain.model.purchase.CartItem
import org.json4s.DefaultReaders._
import org.json4s._

object CartFormats {

  implicit object CartItemFormat extends Writer[CartItem] with Reader[CartItem] {

    def read(value: JValue): CartItem = CartItem(
      pet = (value \ "pet").as[Item],
      quantity = (value \ "quantity").as[Int],
      inStock = (value \ "isInStock").as[Boolean]
    )

    def write(obj: CartItem): JValue =
      JObject(
        JField("pet", obj.pet.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("isInStock", JBool(obj.inStock))
      )

  }

}
