package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.pet.Pet
import com.github.j5ik2o.spetstore.domain.purchase.OrderItem
import org.json4s._
import org.json4s.DefaultReaders._
import PetFormats._

object OrderFormats {

  implicit object OrderItemFormat extends Writer[OrderItem] with Reader[OrderItem] {

    def write(obj: OrderItem): JValue =
      JObject(
        JField("pet", obj.pet.asJValue),
        JField("quantity", JInt(obj.quantity))
      )

    def read(value: JValue): OrderItem = OrderItem(
      pet = (value \ "pet").as[Pet],
      quantity = (value \ "quantity").as[Int]
    )

  }

}
