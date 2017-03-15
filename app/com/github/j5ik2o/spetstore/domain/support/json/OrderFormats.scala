package com.github.j5ik2o.spetstore.domain.support.json

import IdentifierFormats._
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{ OrderItemId, OrderItem }
import org.json4s.DefaultReaders._
import org.json4s._

object OrderFormats {

  implicit object OrderItemFormat extends Writer[OrderItem] with Reader[OrderItem] {

    def write(obj: OrderItem): JValue =
      JObject(
        JField("id", JString(obj.id.value.toString)),
        JField("no", JInt(obj.no)),
        JField("status", JInt(obj.status.id)),
        JField("itemId", obj.itemId.asJValue),
        JField("quantity", JInt(obj.quantity)),
        JField("version", obj.version.map(e => JString(e.toString)).getOrElse(JNull))
      )

    def read(value: JValue): OrderItem = OrderItem(
      id = OrderItemId((value \ "id").as[String].toLong),
      no = (value \ "no").as[Int],
      status = StatusType((value \ "status").as[Int]),
      itemId = (value \ "itemId").as[ItemId],
      quantity = (value \ "quantity").as[Int],
      version = (value \ "version").as[Option[String]].map(_.toLong)
    )

  }

}
