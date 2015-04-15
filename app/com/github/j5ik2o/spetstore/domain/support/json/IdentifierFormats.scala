package com.github.j5ik2o.spetstore.domain.support.json

import com.github.j5ik2o.spetstore.domain.support.support.Identifier
import com.github.j5ik2o.spetstore.domain.model.item.{CategoryId, SupplierId, ItemTypeId, ItemId}
import org.json4s.DefaultReaders._
import org.json4s._

object IdentifierFormats {

  case class IdentifierFormat[A <: Identifier[_]](apply: Long => A)
    extends Writer[A] with Reader[A] {

    def write(obj: A): JValue = JString(obj.value.toString)

    def read(value: JValue): A = apply((value \ "id").as[Long])

  }

  implicit val itemIdFormat = IdentifierFormat(ItemId.apply)
  implicit val itemTypeIdFormat = IdentifierFormat(ItemTypeId.apply)
  implicit val categoryIdFormat = IdentifierFormat(CategoryId.apply)
  implicit val supplierIdFormat = IdentifierFormat(SupplierId.apply)

}
