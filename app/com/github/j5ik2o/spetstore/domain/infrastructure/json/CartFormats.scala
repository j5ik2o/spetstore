package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.pet.Pet
import com.github.j5ik2o.spetstore.domain.purchase.CartItem
import org.json4s.DefaultReaders._
import org.json4s._
import PetFormats._

object CartFormats {

  implicit object CartItemFormat extends Writer[CartItem] with Reader[CartItem] {

    def write(obj: CartItem): JValue =
      JObject(
        JField("pet", obj.pet.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("isInStock", JBool(obj.inStock))
      )

    def read(value: JValue): CartItem = CartItem(
      pet = (value \ "pet").as[Pet],
      quantity = (value \ "quantity").as[Int],
      inStock = (value \ "isInStock").as[Boolean]
    )

  }

}
