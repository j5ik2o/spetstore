package com.github.j5ik2o.spetstore.domain.infrastructure.json

import org.json4s._
import org.json4s.DefaultReaders._
import com.github.j5ik2o.spetstore.domain.model.item.{SupplierId, ItemTypeId, ItemId, Item}
import IdentifierFormats._
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType

object PetFormats {

  implicit object PetFormat extends Writer[Item] with Reader[Item] {
    def write(obj: Item): JValue =
      JObject(
        JField("id", obj.id.asJValue),
        JField("status", JInt(obj.status.id)),
        JField("itemTypeId", obj.itemTypeId.asJValue),
        JField("name", JString(obj.name)),
        JField("description", obj.description.map(JString).getOrElse(JNull)),
        JField("price", JDecimal(obj.price)),
        JField("supplierId", obj.supplierId.asJValue)
      )

    def read(value: JValue): Item = Item(
      id = (value \ "id").as[ItemId],
      status = StatusType((value \ "status").as[Int]),
      itemTypeId = (value \ "itemTypeId").as[ItemTypeId],
      name = (value \ "name").as[String],
      description = (value \ "description").as[Option[String]],
      price = (value \ "price").as[BigDecimal],
      supplierId = (value \ "supplierId").as[SupplierId]
    )
  }

}
