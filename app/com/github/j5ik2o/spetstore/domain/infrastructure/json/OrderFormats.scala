package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.model.item.Item
import com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem
import org.json4s._
import org.json4s.DefaultReaders._
import PetFormats._

object OrderFormats {

  implicit object OrderItemFormat extends Writer[OrderItem] with Reader[OrderItem] {

    def write(obj: OrderItem): JValue =
      JObject(
        JField("item", obj.item.asJValue),
        JField("quantity", JInt(obj.quantity))
      )

    def read(value: JValue): OrderItem = OrderItem(
      item = (value \ "item").as[Item],
      quantity = (value \ "quantity").as[Int]
    )

  }

}
