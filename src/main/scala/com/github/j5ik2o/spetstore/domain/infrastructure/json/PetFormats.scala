package com.github.j5ik2o.spetstore.domain.infrastructure.json

import org.json4s._
import org.json4s.DefaultReaders._
import com.github.j5ik2o.spetstore.domain.pet.{PetTypeId, PetId, Pet}
import IdentifierFormats._

object PetFormats {

  implicit object ItemFormat extends Writer[Pet] with Reader[Pet] {
    def write(obj: Pet): JValue =
      JObject(
        JField("id", obj.id.asJValue),
        JField("petTypeId", obj.petTypeId.asJValue),
        JField("name", JString(obj.name)),
        JField("description", obj.description.map(JString).getOrElse(JNull)),
        JField("price", JDecimal(obj.price)),
        JField("quantity", JInt(obj.quantity))
      )

    def read(value: JValue): Pet = Pet(
      id = (value \ "id").as[PetId],
      petTypeId = (value \ "petTypeId").as[PetTypeId],
      name = (value \ "name").as[String],
      description = (value \ "description").as[Option[String]],
      price = (value \ "price").as[BigDecimal],
      quantity = (value \ "quantity").as[Int]
    )
  }

}
