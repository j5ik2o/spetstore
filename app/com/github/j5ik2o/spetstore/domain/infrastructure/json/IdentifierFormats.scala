package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Identifier
import java.util.UUID
import org.json4s._
import org.json4s.DefaultReaders._
import com.github.j5ik2o.spetstore.domain.model.pet.{SupplierId, PetTypeId, PetId}

object IdentifierFormats {

  case class IdentifierFormat[A <: Identifier[_]](apply: UUID => A)
    extends Writer[A] with Reader[A] {

    def write(obj: A): JValue = JString(obj.value.toString)

    def read(value: JValue): A = apply(UUID.fromString((value \ "id").as[String]))

  }

  implicit val petIdFormat = IdentifierFormat(PetId.apply)
  implicit val petTypeIdFormat = IdentifierFormat(PetTypeId.apply)
  implicit val supplierIdFormat = IdentifierFormat(SupplierId.apply)

}
