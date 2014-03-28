package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem
import org.json4s._
import org.json4s.DefaultReaders._
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import IdentifierFormats._

object OrderFormats {

  implicit object OrderItemFormat extends Writer[OrderItem] with Reader[OrderItem] {

    def write(obj: OrderItem): JValue =
      JObject(
        JField("no", JInt(obj.no)),
        JField("status", JInt(obj.status.id)),
        JField("itemId", obj.itemId.asJValue),
        JField("quantity", JInt(obj.quantity))
      )

    def read(value: JValue): OrderItem = OrderItem(
      no = (value \ "no").as[Long],
      status = StatusType((value \ "status").as[Int]),
      itemId = (value \ "itemId").as[ItemId],
      quantity = (value \ "quantity").as[Int]
    )

  }

}
