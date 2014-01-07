package com.github.j5ik2o.spetstore.domain.infrastructure.json

import org.json4s._
import org.json4s.DefaultReaders._
import org.json4s.DefaultWriters._
import com.github.j5ik2o.spetstore.domain.model.pet.{SupplierId, PetTypeId, PetId, Pet}
import IdentifierFormats._
import com.github.j5ik2o.spetstore.domain.model.basic.SexType

object PetFormats {

  implicit object PetFormat extends Writer[Pet] with Reader[Pet] {
    def write(obj: Pet): JValue =
      JObject(
        JField("id", obj.id.asJValue),
        JField("petTypeId", obj.petTypeId.asJValue),
        JField("sexType", JInt(obj.sexType.id)),
        JField("name", JString(obj.name)),
        JField("description", obj.description.map(JString).getOrElse(JNull)),
        JField("price", JDecimal(obj.price)),
        JField("supplierId", obj.supplierId.asJValue)
      )

    def read(value: JValue): Pet = Pet(
      id = (value \ "id").as[PetId],
      petTypeId = (value \ "petTypeId").as[PetTypeId],
      sexType = SexType((value \ "sexType").as[Int]),
      name = (value \ "name").as[String],
      description = (value \ "description").as[Option[String]],
      price = (value \ "price").as[BigDecimal],
      supplierId = (value \ "supplierId").as[SupplierId]
    )
  }

}
